from fastapi import APIRouter, Depends
import pandas as pd
from src.datasource import irweb_data
from collections import Counter
from typing import Optional

router = APIRouter()

@router.get("/grade/helloworld")
async def helloworld_grade(
    lecture_name: Optional[str] = "インターネットとウェブテクノロジー",
    db_connection=Depends(irweb_data)
):
    # lecture_nameにデフォルト値を設定しました。
    # これにより、?lecture_name=... を付けずにアクセスしても422エラーにならず動作します。

    # 1. データベースクエリの実行
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
        # データベース側のエラー（カラム名不一致など）を捕捉します。
        return {"error": "Query failed", "detail": str(e)}

    # 2. データの存在確認
    # 該当する講義名が1件も存在しない場合、処理を中断してメッセージを返します。
    if df is None or df.empty:
        return {"data": {}, "message": f"No data found for lecture: {lecture_name}"}

    try:
        # 3. 成績評価（grading）の集計
        # カラム「grading」の値を集計し、各評価の人数を辞書形式にします。
        grading_counts = dict(Counter(df["grading"].values))

        # 4. 代表値（1行目のデータ）の抽出
        # 統計以外の固定情報（教員名など）をrowに格納します。
        row = df.iloc[0]

        # 5. 安全な型変換関数（内部関数）
        # NULL値(NaN)が含まれている場合に、int()やfloat()でクラッシュするのを防ぎます。
        def safe_int(val):
            n = pd.to_numeric(val, errors='coerce')
            return int(n) if pd.notnull(n) else 0

        def safe_float(val):
            n = pd.to_numeric(val, errors='coerce')
            return float(n) if pd.notnull(n) else 0.0

        def safe_str(val):
            return str(val) if pd.notnull(val) else ""

        # 6. レスポンス用データの構築
        # フロントエンドが期待するデータ構造に整理します。
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
        # データの加工中に発生した予期せぬエラーを捕捉します。
        return {"error": "Processing failed", "detail": str(e)}