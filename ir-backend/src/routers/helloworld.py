from fastapi import APIRouter, Depends
import pandas as pd
from src.datasource import irweb_data
from collections import Counter
from typing import Optional

router = APIRouter()

# ---------------------------------------------------------
# 1. 授業詳細（成績分布など）を取得するエンドポイント
# ---------------------------------------------------------
@router.get("/grade/helloworld")
async def helloworld_grade(
        lecture_name: Optional[str] = "インターネットとウェブテクノロジー",
        db_connection=Depends(irweb_data)
):
    try:
        df = await db_connection.query("grade_new",
                                       "target_grade",
                                       "available_semester",
                                       "target_department",
                                       "lecture_name",
                                       "lecture_teacher",
                                       "number_credits_course",
                                       "attendance",
                                       "grading",
                                       where_and={"lecture_name": lecture_name})
    except Exception as e:
        return {"error": "Query failed", "detail": str(e)}

    if df is None or df.empty:
        return {"data": {}, "message": f"No data found for lecture: {lecture_name}"}

    try:
        grading_counts = dict(Counter(df["grading"].values))
        row = df.iloc[0]

        def safe_int(val):
            n = pd.to_numeric(val, errors='coerce')
            return int(n) if pd.notnull(n) else 0

        def safe_float(val):
            n = pd.to_numeric(val, errors='coerce')
            return float(n) if pd.notnull(n) else 0.0

        def safe_str(val):
            return str(val) if pd.notnull(val) else ""

        result = {
            "grading_distribution": grading_counts,
            "lecture_teacher": safe_str(row["lecture_teacher"]),
            "lecture_name": safe_str(row["lecture_name"]),
            "target_department": safe_str(row["target_department"]),
            "target_grade": str(safe_int(row["target_grade"])),
            "available_semester": str(safe_int(row["available_semester"])),
            "number_credits_course": safe_float(row["number_credits_course"]),
            "attendance": safe_int(row["attendance"])
        }
        return {"data": result}
    except Exception as e:
        return {"error": "Processing failed", "detail": str(e)}


# ---------------------------------------------------------
# 2. マスターリスト（選択肢一覧）を取得するエンドポイント
# ---------------------------------------------------------
@router.get("/grade/master_list")
async def get_master_list(db_connection=Depends(irweb_data)):
    try:
        df = await db_connection.query("grade_new",
                                       "target_grade",
                                       "available_semester",
                                       "target_department",
                                       "lecture_name",
                                       "lecture_teacher",
                                       "number_credits_course"
                                       )
    except Exception as e:
        return {"error": "Query failed", "detail": str(e)}

    if df is None or df.empty:
        return {"data": {}, "message": "データベースにレコードが存在しません"}

    def extract_unique_list(series, is_numeric=False):
        valid_data = series.dropna().astype(str).unique()
        processed_list = []
        for item in valid_data:
            if is_numeric:
                try:
                    num = float(item)
                    if pd.notnull(num):
                        processed_list.append(str(int(num)))
                except ValueError:
                    continue
            else:
                if item.strip() and item.lower() != "nan":
                    processed_list.append(item.strip())
        return sorted(list(set(processed_list)))

    try:
        result = {
            "target_grades": extract_unique_list(df["target_grade"], is_numeric=True),
            "available_semesters": extract_unique_list(df["available_semester"], is_numeric=True),
            "target_departments": extract_unique_list(df["target_department"]),
            "lecture_names": extract_unique_list(df["lecture_name"]),
            "lecture_teachers": extract_unique_list(df["lecture_teacher"]),
            "number_credits_courses": extract_unique_list(df["number_credits_course"], is_numeric=True)
        }
        return {"data": result}
    except Exception as e:
        return {"error": "Processing failed", "detail": str(e)}


# ---------------------------------------------------------
# 3. 授業検索エンドポイント（論理比較・不一致対策版）
# ---------------------------------------------------------
@router.get("/grade/search")
async def search_grade(
        target_grade: str,
        available_semester: str,
        target_department: str,
        db_connection=Depends(irweb_data)
):
    try:
        # DB側の型（double precision）に依存した厳密比較を避けるため、一旦全件（必要なカラム）取得します。
        df = await db_connection.query(
            "grade_new",
            "lecture_name",
            "lecture_teacher",
            "number_credits_course",
            "target_grade",
            "available_semester",
            "target_department"
        )

        if df is None or df.empty:
            return {"results": []}

        # double precision型 (3.0など) を文字列 ("3") に変換する関数です。
        # これにより、DB上の 3.0 と Java側の "3" を論理的に一致させます。
        def normalize(val):
            try:
                # float変換 -> int変換 -> 文字列変換により、3.000... を "3" に揃えます。
                return str(int(float(val)))
            except:
                return str(val).strip()

        # Pandasの機能を利用して、Javaからの入力値と論理的に比較します。
        # 学科名は前後の空白を除去して比較、学年と学期は正規化後の文字列で比較します。
        mask = (
                (df["target_grade"].apply(normalize) == str(target_grade)) &
                (df["available_semester"].apply(normalize) == str(available_semester)) &
                (df["target_department"].astype(str).str.strip() == target_department.strip())
        )

        filtered_df = df[mask].copy()

        if filtered_df.empty:
            # デバッグ用ログ：不一致時の条件をサーバー側のコンソールに出力します。
            print(f"Mismatch: Grade={target_grade}, Sem={available_semester}, Dept={target_department}")
            return {"results": []}

        # 授業名の重複を排除します。
        filtered_df = filtered_df.drop_duplicates(subset=["lecture_name"])

        # JavaのGrid用DTO形式に整形します。
        results = []
        for _, row in filtered_df.iterrows():
            results.append({
                "lecture_name": str(row["lecture_name"]) if pd.notnull(row["lecture_name"]) else "",
                "lecture_teacher": str(row["lecture_teacher"]) if pd.notnull(row["lecture_teacher"]) else "",
                "number_credits_course": normalize(row["number_credits_course"])
            })

        print(f"Success: Found {len(results)} items")
        return {"results": results}

    except Exception as e:
        print(f"Error in search_grade: {str(e)}")
        return {"error": "Search failed", "detail": str(e)}