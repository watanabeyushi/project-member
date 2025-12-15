# 必要なモジュールのimport
import pandas as pd
import asyncio

# jsonを取得する関数
async def get_json_description(df_data, subject_id:str):
    # データ取ってきて絞り込み
    df = df_data
    df_lec = df[df["course_id"] == subject_id].reset_index(drop=True)

    if df_lec["title"][0] == "振り返りと授業評価アンケート (実験・実習)":
        q7 = df_lec["answer_7_1"]
        q17 = df_lec["answer_17_1"]
        q18 = df_lec["answer_18_1"]
        q19 = df_lec["answer_19_1"]
        q7 = q7.dropna()
        q17 = q17.dropna()
        q18 = q18.dropna()
        q19 = q19.dropna()

        df_description = {"科目名":df_lec["lecture_name"], "担当者":df_lec["lecture_teacher"], "q7":q7, "q17":q17, "q18":q18, "q19":q19}

    elif df_lec["title"][0] == "振り返りと授業評価アンケート (対面・オンライン)":
        q17 = df_lec["answer_17_1"]
        q18 = df_lec["answer_18_1"]
        q19 = df_lec["answer_19_1"]
        q17 = q17.dropna()
        q18 = q18.dropna()
        q19 = q19.dropna()

        df_description = {"科目名": df_lec["lecture_name"], "担当者": df_lec["lecture_teacher"], "q17": q17, "q18": q18, "q19": q19}

    return df_description
