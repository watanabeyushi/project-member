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


@router.get("/grade/master_list")
async def get_master_list(db_connection=Depends(irweb_data)):
    # 1. データベースから全件取得を試みます
    try:
        # 全データを取得します（where_andを指定しないことで全件抽出）
        df = await db_connection.query("grade_new",
                                       "target_grade",
                                       "available_semester",
                                       "target_department",
                                       "lecture_name")
    except Exception as e:
        return {"error": "Query failed", "detail": str(e)}

    # 2. データが空の場合のチェック
    if df is None or df.empty:
        return {"data": {}, "message": "データベースにレコードが存在しません"}

    # 3. リスト作成のための汎用的な整形関数
    def extract_unique_list(series, is_numeric=False):
        # 欠損値を除去し、すべて文字列に変換します
        valid_data = series.dropna().astype(str).unique()

        processed_list = []
        for item in valid_data:
            if is_numeric:
                # 数値を含む文字列（"1.0"など）を整数文字列（"1"）に整形します
                try:
                    num = float(item)
                    # NaNや無限大を除外して整数化
                    if pd.notnull(num):
                        processed_list.append(str(int(num)))
                except ValueError:
                    continue
            else:
                # 文字列データから空文字や "nan" を除外
                if item.strip() and item.lower() != "nan":
                    processed_list.append(item.strip())

        # 重複を排除して昇順に並べ替えます
        return sorted(list(set(processed_list)))

    try:
        # 4. 各項目のリストを生成
        result = {
            "target_grades": extract_unique_list(df["target_grade"], is_numeric=True),
            "available_semesters": extract_unique_list(df["available_semester"], is_numeric=True),
            "target_departments": extract_unique_list(df["target_department"]),
            "lecture_names": extract_unique_list(df["lecture_name"])
        }

        return {"data": result}

    except Exception as e:
        return {"error": "Processing failed", "detail": str(e)}