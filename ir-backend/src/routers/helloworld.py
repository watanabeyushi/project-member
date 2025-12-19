from fastapi import APIRouter, Depends
import pandas as pd
import json
from src.datasource import irweb_data, irweb_users
from collections import Counter

router = APIRouter()

# ここにhelloworld_grade関数を作成
@router.get("/grade/helloworld")
async def helloworld_grade(db_connection=Depends(irweb_data)):
    # 指定された8つのカラムを取得します。
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
                                       where_and={"course_id": 'jhPZad'})
    except Exception as e:
        # クエリ実行失敗時の詳細を返却します。
        return {"error": "Query failed", "detail": str(e)}

    # 検索結果が存在しない場合の処理です。
    if df is None or df.empty:
        return {"data": {}, "message": "No data found"}

    try:
        # 1. 成績評価（grading）の分布を集計します。
        grading_counts = dict(Counter(df["grading"].values))

        # 2. 代表値（1行目のデータ）を安全に取得します。
        row = df.iloc[0]

        # 3. 各値を個別に処理し、NaNが含まれる場合に備えた安全な変換関数を適用します。
        def safe_int(val):
            # 数値に変換し、変換不能な場合やNaNの場合は0を返します。
            n = pd.to_numeric(val, errors='coerce')
            return int(n) if pd.notnull(n) else 0

        def safe_float(val):
            # 数値に変換し、NaNの場合は0.0を返します。
            n = pd.to_numeric(val, errors='coerce')
            return float(n) if pd.notnull(n) else 0.0

        def safe_str(val):
            # 欠損値（NaN/None）の場合は空文字、それ以外は文字列として返します。
            return str(val) if pd.notnull(val) else ""

        # 結果を辞書に格納します。
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
        # 変換処理中に発生したエラーを捕捉します。
        return {"error": "Processing failed", "detail": str(e)}