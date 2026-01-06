from fastapi import APIRouter, Depends, Query
import pandas as pd
from src.datasource import irweb_data
from collections import Counter
from typing import Optional

router = APIRouter()


# ---------------------------------------------------------
# 1. 授業詳細エンドポイント
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
                                       "available_year",
                                       "compulsory_subjects",
                                       where_and={"lecture_name": lecture_name})
    except Exception as e:
        return {"error": "Query failed", "detail": str(e)}

    if df is None or df.empty:
        print(f"DEBUG: Data is empty for {lecture_name}")  # 空の場合もログに出す
        return {"data": {}, "message": f"No data found for lecture: {lecture_name}"}

    # =========================================================
    # ▼▼▼ デバッグ用コード追加 ▼▼▼
    # =========================================================
    print("\n" + "=" * 30)
    print(f"DEBUG: Data for '{lecture_name}'")
    print(f"Total Rows: {len(df)}")

    # attendanceカラムのデータ型を確認
    print(f"Attendance Dtype: {df['attendance'].dtype}")

    # attendanceに入っているユニークな値を確認（変な値が混じっていないか）
    print("Unique values in 'attendance':")
    print(df['attendance'].unique())

    # attendance と grading の組み合わせを最初の10行だけ表示
    print("Top 10 rows (attendance vs grading):")
    print(df[['attendance', 'grading']].head(10))
    print("=" * 30 + "\n")
    # =========================================================

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

        # クロス集計
        cross_tab = pd.crosstab(df['attendance'], df['grading'])
        crosstab_data = cross_tab.to_dict(orient='index')

        # クロス集計結果もデバッグ表示
        print("DEBUG: Generated Crosstab Data:")
        print(crosstab_data)

        result = {
            "grading_distribution": grading_counts,
            "lecture_teacher": safe_str(row["lecture_teacher"]),
            "lecture_name": safe_str(row["lecture_name"]),
            "target_department": safe_str(row["target_department"]),
            "target_grade": str(safe_int(row["target_grade"])),
            "available_semester": str(safe_int(row["available_semester"])),
            "number_credits_course": safe_float(row["number_credits_course"]),
            "attendance": safe_int(row["attendance"]),
            "year": safe_int(row["available_year"]),
            "classification": safe_str(row["compulsory_subjects"]),
            "crosstab": crosstab_data
        }
        return {"data": result}
    except Exception as e:
        import traceback
        traceback.print_exc()
        return {"error": "Processing failed", "detail": str(e)}


# ---------------------------------------------------------
# 2. マスターリスト（選択肢一覧）を取得するエンドポイント
# ---------------------------------------------------------
@router.get("/grade/master_list")
async def get_master_list(db_connection=Depends(irweb_data)):
    try:
        # データベースへのクエリに 'compulsory_subjects' と 'available_year' を追加
        df = await db_connection.query("grade_new",
                                       "target_grade",
                                       "available_semester",
                                       "target_department",
                                       "lecture_name",
                                       "lecture_teacher",
                                       "number_credits_course",
                                       "compulsory_subjects",  # 追加: 必修区分
                                       "available_year"  # 追加: 開講年度
                                       )
    except Exception as e:
        return {"error": "Query failed", "detail": str(e)}

    if df is None or df.empty:
        return {"data": {}, "message": "データベースにレコードが存在しません"}

    # ユニークな値のリストを抽出するヘルパー関数（変更なし）
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
        # レスポンスのJSONに新しいキーを追加
        result = {
            "target_grades": extract_unique_list(df["target_grade"], is_numeric=True),
            "available_semesters": extract_unique_list(df["available_semester"], is_numeric=True),
            "target_departments": extract_unique_list(df["target_department"]),
            "lecture_names": extract_unique_list(df["lecture_name"]),
            "lecture_teachers": extract_unique_list(df["lecture_teacher"]),
            "number_credits_courses": extract_unique_list(df["number_credits_course"], is_numeric=True),

            # 追加部分
            "compulsory_subjects": extract_unique_list(df["compulsory_subjects"]),  # 必修区分（文字列扱い）
            "available_years": extract_unique_list(df["available_year"], is_numeric=True)  # 開講年度（数値扱い）
        }
        return {"data": result}
    except Exception as e:
        return {"error": "Processing failed", "detail": str(e)}


# ---------------------------------------------------------
# 3. 授業検索エンドポイント（修正版：重複削除を追加）
# ---------------------------------------------------------
@router.get("/grade/search")
async def search_grade(
        grade: str = Query(..., description="学年 (allの場合は全件)"),
        dept: str = Query(..., description="学科 (allの場合は全件)"),
        classification: str = Query(..., description="必修区分 (allの場合は全件)"),
        db_connection=Depends(irweb_data)
):
    try:
        # 1. データベースからデータ取得
        df = await db_connection.query(
            "grade_new",
            "lecture_name",
            "available_year",
            "target_grade",
            "target_department",
            "compulsory_subjects",
            "number_credits_course"
        )

        if df is None or df.empty:
            return []

        # 2. フィルタリング準備
        mask = pd.Series(True, index=df.index)

        def normalize(val):
            if pd.isna(val): return ""
            try:
                return str(int(float(val)))
            except:
                return str(val).strip()

        # 3. 条件適用
        if grade != "all":
            mask &= (df["target_grade"].apply(normalize) == str(grade))

        if dept != "all":
            mask &= (df["target_department"].astype(str).str.strip() == dept.strip())

        if classification != "all":
            mask &= (df["compulsory_subjects"].astype(str).str.strip() == classification.strip())

        filtered_df = df[mask].copy()

        if filtered_df.empty:
            return []

        # -----------------------------------------------------------
        # 【修正ポイント】 重複データの削除
        # 同じ科目名・年度・学科・学年・区分のデータは1つにまとめます
        # -----------------------------------------------------------
        filtered_df = filtered_df.drop_duplicates(subset=[
            "lecture_name",
            "available_year",
            "target_grade",
            "target_department",
            "compulsory_subjects"
        ])

        # 5. 結果のJSON作成
        results = []
        for _, row in filtered_df.iterrows():
            results.append({
                "subject_name": str(row["lecture_name"]) if pd.notnull(row["lecture_name"]) else "",
                "year": int(row["available_year"]) if pd.notnull(row["available_year"]) else 0,
                "target_grade": normalize(row["target_grade"]),
                "department": str(row["target_department"]) if pd.notnull(row["target_department"]) else "",
                "classification": str(row["compulsory_subjects"]) if pd.notnull(row["compulsory_subjects"]) else "",
                "credits": int(float(row["number_credits_course"])) if pd.notnull(row["number_credits_course"]) else 0
            })

        print(f"Search success: {len(results)} records found.")
        return results

    except Exception as e:
        print(f"Error in search_grade: {str(e)}")
        return []