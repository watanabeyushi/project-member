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
# 3. 条件に合致する授業一覧を検索するエンドポイント（追加分）
# ---------------------------------------------------------
@router.get("/grade/search")
async def search_grade(
        target_grade: str,
        available_semester: str,
        target_department: str,
        db_connection=Depends(irweb_data)
):
    try:
        # 指定された3条件でデータベースからデータを取得します
        df = await db_connection.query(
            "grade_new",
            "lecture_name",
            "lecture_teacher",
            "number_credits_course",
            where_and={
                "target_grade": target_grade,
                "available_semester": available_semester,
                "target_department": target_department
            }
        )

        if df is None or df.empty:
            return {"results": []}

        # 授業名の重複を排除します
        df = df.drop_duplicates(subset=["lecture_name"])

        # Java側のDTO形式に合わせてリストを作成します
        results = []
        for _, row in df.iterrows():
            results.append({
                "lecture_name": str(row["lecture_name"]) if pd.notnull(row["lecture_name"]) else "",
                "lecture_teacher": str(row["lecture_teacher"]) if pd.notnull(row["lecture_teacher"]) else "",
                "number_credits_course": str(int(float(row["number_credits_course"]))) if pd.notnull(
                    row["number_credits_course"]) else "0"
            })
        return {"results": results}
    except Exception as e:
        return {"error": "Search failed", "detail": str(e)}