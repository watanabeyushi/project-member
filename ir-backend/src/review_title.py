# 必要なモジュールのimport
import pandas as pd
import asyncio

# df作成
async def df_title(df_data, subject_id : str):

    # データの取得
    df = df_data

    # コースIDで絞り込み
    df_ = df[df["course_id"] == subject_id].reset_index()

    # 質問項目を取り出す
    titles = []
    for i in range(4, 20):
        if i == 6:
            titles.append(df_["question_" + str(i)][2])
        else:
            titles.append(df_["question_" + str(i)][0])

    # 質問項目のdf作成
    df_title = pd.DataFrame(titles, columns=["title"])

    return df_title