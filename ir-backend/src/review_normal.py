# 必要なモジュールのimport
import pandas as pd
import asyncio

# 項目名をとってくる関数
async def index(df_data, subject_id, question_number: str):
    # データの取得
    df_ = df_data

    # 科目コードで絞り込み
    df = df_[df_["course_id"] == subject_id].reset_index(drop=True)
    df = df.rename(
        columns={'answer_4_1': 'q4', 'answer_5_1': 'q5', 'answer_6_1': 'q6', 'answer_7_1': 'q7', 'answer_8_1': 'q8', 'answer_9_1': 'q9',
                 'answer_10_1': 'q10', 'answer_11_1': 'q11', 'answer_12_1': 'q12', 'answer_13_1': 'q13',
                 'answer_14_1': 'q14', 'answer_15_1': 'q15', 'answer_16_1': 'q16', 'answer_17_1': 'q17', 'answer_18_1': 'q18', 'answer_19_1': 'q19'})

    # 質問番号に対応するインデックス辞書を定義
    experiment_index_dict = {
        "q4": ["難しかった", "やや難しかった", "丁度良かった", "少し易しかった", "易しかった"],
        "q5": ["多かった", "やや多かった", "丁度良かった", "やや少なかった", "少なかった"],
        "q6": ["速かった", "少し早かった", "丁度良かった", "少し遅かった", "遅かった"],
        "q8": ["毎回欠かさず事前学習をした", "かなりの頻度で事前学習をした", "半分程度事前学習をした",
               "時々事前学習をした", "全くしなかった"],
        "q9": ["他の授業より積極的に取り組んだ", "取り組んだ", "まあまあ取り組んだ", "あまり取り組まなかった",
               "ほとんど取り組まなかった"],
        "q10": ["100 % ～ 90 % 程度", "80 % 程度", "70 % 程度", "60 % 程度", "50 % 程度", "40 % 以下"],
        "q11": ["理解し易かった", "概ね理解し易かった", "どちらともいえない", "少し理解しにくかった",
                "理解しにくかった"],
        "q12": ["理解し易かった", "概ね理解し易かった", "どちらともいえない", "少し理解しにくかった",
                "理解しにくかった"],
        "q13": ["整っていた", "概ね整っていた", "どちらともいえない", "あまり整っていなかった", "整っていなかった"],
        "q14": ["適切だった", "概ね適切だった", "どちらともいえない", "あまり適切でなかった", "適切でなかった", "授業を通じて質問はしなかった"],
        "q15": ["適切だった", "概ね適切だった", "どちらともいえない", "あまり適切でなかった", "アドバイスはなかった"],
        "q16": ["満足", "概ね満足", "どちらともいえない", "やや不満", "不満"]
    }

    normal_index_dict = {
        "q4": ["難しかった", "やや難しかった", "丁度良かった", "少し易しかった", "易しかった"],
        "q5": ["多かった", "やや多かった", "丁度良かった", "やや少なかった", "少なかった"],
        "q6": ["速かった", "少し早かった", "丁度良かった", "少し遅かった", "遅かった"],
        "q7": ["かなり持てた", "持てた", "少し持てた", "あまり持てなかった", "持てなかった"],
        "q8": ["４時間以上", "３時間以上～４時間未満", "２時間以上～３時間未満", "１時間以上～２時間未満", "３０分以上～１時間未満", "３０分未満"],
        "q9": ["100 % ～ 90 % 程度", "80 % 程度", "70 % 程度", "60 % 程度", "50 % 程度", "40 % 以下"],
        "q10": ["シラバスに沿っていた", "概ねシラバスに沿っていた", "どちらともいえない", "あまりシラバスに沿っていなかった", "全くシラバスに沿っていなかった", "シラバスを見ていなかった"],
        "q11": ["理解し易かった", "概ね理解し易かった", "どちらともいえない", "少し理解しにくかった",
                "理解しにくかった"],
        "q12": ["理解に役立った", "少し理解に役立った", "どちらともいえない", "あまり理解に役立たなかった", "理解に役立たなかった"],
        "q13": ["丁寧に説明していた", "概ね丁寧に説明していた", "どちらともいえない", "あまり丁寧に説明していなかった", "全く説明していなかった"],
        "q14": ["丁寧に応じていた", "概ね丁寧に応じていた", "どちらともいえない", "あまり丁寧に応じていなかった", "全く応じていなかった", "授業を通じて質問はしなかった"],
        "q15": ["満足", "概ね満足", "どちらともいえない", "やや不満", "不満"],
        "q16": ["満足した", "概ね満足した", "どちらともいえない", "あまり満足しなかった", "満足しなかった", "オンライン授業は実施されなかった"]
    }

    # 実験・実習か通常かを判別しインデックスを取得
    if df["title"].iloc[0] == "振り返りと授業評価アンケート (実験・実習)":
        return experiment_index_dict.get(question_number, [])
    elif df["title"].iloc[0] == "振り返りと授業評価アンケート (対面・オンライン)":
        return normal_index_dict.get(question_number, [])
    else:
        return []


# replace
async def normal_replace(df_data):
    # データの取得
    df_ = df_data

    # dfの作成
    df = pd.DataFrame(df_)
    df = df.rename(columns={'answer_4_1':'q4', 'answer_5_1':'q5', 'answer_6_1':'q6', 'answer_7_1': 'q7', 'answer_8_1':'q8', 'answer_9_1':'q9', 'answer_10_1':'q10', 'answer_11_1':'q11', 'answer_12_1':'q12', 'answer_13_1':'q13', 'answer_14_1':'q14', 'answer_15_1':'q15', 'answer_16_1':'q16'})

    # replace
    df["title"] = df["title"].replace("振り返りと授業評価アンケート (実験・実習)", "実験・実習")
    df["title"] = df["title"].replace("振り返りと授業評価アンケート (対面・オンライン)", "対面・オンライン")

    df["q4"] = df["q4"].replace("難しかった", 5)
    df["q4"] = df["q4"].replace("やや難しかった", 4)
    df["q4"] = df["q4"].replace("丁度良かった", 3)
    df["q4"] = df["q4"].replace("少し易しかった", 2)
    df["q4"] = df["q4"].replace("易しかった", 1)

    df["q5"] = df["q5"].replace("多かった", 5)
    df["q5"] = df["q5"].replace("やや多かった", 4)
    df["q5"] = df["q5"].replace("丁度良かった", 3)
    df["q5"] = df["q5"].replace("やや少なかった", 2)
    df["q5"] = df["q5"].replace("少なかった", 1)

    df["q6"] = df["q6"].replace("速かった", 5)
    df["q6"] = df["q6"].replace("少し早かった", 4)
    df["q6"] = df["q6"].replace("丁度良かった", 3)
    df["q6"] = df["q6"].replace("少し遅かった", 2)
    df["q6"] = df["q6"].replace("遅かった", 1)

    df["q7"] = df["q7"].replace("かなり持てた", 5.0)
    df["q7"] = df["q7"].replace("持てた", 4.0)
    df["q7"] = df["q7"].replace("少し持てた", 3.0)
    df["q7"] = df["q7"].replace("あまり持てなかった", 2.0)
    df["q7"] = df["q7"].replace("持てなかった", 1.0)

    df["q8"] = df["q8"].replace("毎回欠かさず事前学習をした", 5)
    df["q8"] = df["q8"].replace("かなりの頻度で事前学習をした", 4)
    df["q8"] = df["q8"].replace("半分程度事前学習をした", 3)
    df["q8"] = df["q8"].replace("時々事前学習をした", 2)
    df["q8"] = df["q8"].replace("全くしなかった", 1)
    df["q8"] = df["q8"].replace("４時間以上", 6)
    df["q8"] = df["q8"].replace("３時間以上～４時間未満", 5)
    df["q8"] = df["q8"].replace("２時間以上～３時間未満", 4)
    df["q8"] = df["q8"].replace("１時間以上～２時間未満", 3)
    df["q8"] = df["q8"].replace("３０分以上～１時間未満", 2)
    df["q8"] = df["q8"].replace("３０分未満", 1)

    df["q9"] = df["q9"].replace("他の授業より積極的に取り組んだ", 5)
    df["q9"] = df["q9"].replace("取り組んだ", 4)
    df["q9"] = df["q9"].replace("まあまあ取り組んだ", 3)
    df["q9"] = df["q9"].replace("あまり取り組まなかった", 2)
    df["q9"] = df["q9"].replace("取り組まなかった", 1)
    df["q9"] = df["q9"].replace("100 % ～ 90 % 程度", 6)
    df["q9"] = df["q9"].replace("80 % 程度", 5)
    df["q9"] = df["q9"].replace("70 % 程度", 4)
    df["q9"] = df["q9"].replace("60 % 程度", 3)
    df["q9"] = df["q9"].replace("50 % 程度", 2)
    df["q9"] = df["q9"].replace("40 % 以下", 1)

    df["q10"] = df["q10"].replace("100 % ～ 90 % 程度", 6)
    df["q10"] = df["q10"].replace("80 % 程度", 5)
    df["q10"] = df["q10"].replace("70 % 程度", 4)
    df["q10"] = df["q10"].replace("60 % 程度", 3)
    df["q10"] = df["q10"].replace("50 % 程度", 2)
    df["q10"] = df["q10"].replace("40 % 以下", 1)
    df["q10"] = df["q10"].replace("シラバスに沿っていた", 6)
    df["q10"] = df["q10"].replace("概ねシラバスに沿っていた", 5)
    df["q10"] = df["q10"].replace("どちらともいえない", 4)
    df["q10"] = df["q10"].replace("あまりシラバスに沿っていなかった", 3)
    df["q10"] = df["q10"].replace("全くシラバスに沿っていなかった", 2)
    df["q10"] = df["q10"].replace("シラバスを見ていなかった", 1)

    df["q11"] = df["q11"].replace("理解し易かった", 5)
    df["q11"] = df["q11"].replace("概ね理解し易かった", 4)
    df["q11"] = df["q11"].replace("どちらともいえない", 3)
    df["q11"] = df["q11"].replace("少し理解しにくかった", 2)
    df["q11"] = df["q11"].replace("理解しにくかった", 1)

    df["q12"] = df["q12"].replace("理解し易かった", 5)
    df["q12"] = df["q12"].replace("概ね理解し易かった", 4)
    df["q12"] = df["q12"].replace("どちらともいえない", 3)
    df["q12"] = df["q12"].replace("少し理解しにくかった", 2)
    df["q12"] = df["q12"].replace("理解しにくかった", 1)
    df["q12"] = df["q12"].replace("理解に役立った", 5)
    df["q12"] = df["q12"].replace("少し理解に役立った", 4)
    df["q12"] = df["q12"].replace("あまり理解に役立たなかった", 2)
    df["q12"] = df["q12"].replace("理解に役立たなかった", 1)

    df["q13"] = df["q13"].replace("整っていた", 5)
    df["q13"] = df["q13"].replace("概ね整っていた", 4)
    df["q13"] = df["q13"].replace("どちらともいえない", 3)
    df["q13"] = df["q13"].replace("あまり整っていなかった", 2)
    df["q13"] = df["q13"].replace("整っていなかった", 1)
    df["q13"] = df["q13"].replace("丁寧に説明していた", 5)
    df["q13"] = df["q13"].replace("概ね丁寧に説明していた", 4)
    df["q13"] = df["q13"].replace("あまり丁寧に説明していなかった", 2)
    df["q13"] = df["q13"].replace("全く説明していなかった", 1)

    df["q14"] = df["q14"].replace("適切だった", 5)
    df["q14"] = df["q14"].replace("概ね適切だった", 4)
    df["q14"] = df["q14"].replace("どちらともいえない", 3)
    df["q14"] = df["q14"].replace("あまり適切でなかった", 2)
    df["q14"] = df["q14"].replace("適切でなかった", 1)
    df["q14"] = df["q14"].replace("丁寧に応じていた", 6)
    df["q14"] = df["q14"].replace("概ね丁寧に応じていた", 5)
    df["q14"] = df["q14"].replace("あまり丁寧に応じていなかった", 4)
    df["q14"] = df["q14"].replace("全く応じていなかった", 2)
    df["q14"] = df["q14"].replace("授業を通じて質問はしなかった", 1)

    df["q15"] = df["q15"].replace("適切だった", 5)
    df["q15"] = df["q15"].replace("概ね適切だった", 4)
    df["q15"] = df["q15"].replace("どちらともいえない", 3)
    df["q15"] = df["q15"].replace("あまり適切でなかった", 2)
    df["q15"] = df["q15"].replace("適切でなかった", 1)
    df["q15"] = df["q15"].replace("満足", 5)
    df["q15"] = df["q15"].replace("概ね満足", 4)
    df["q15"] = df["q15"].replace("やや不満", 2)
    df["q15"] = df["q15"].replace("不満", 1)

    df["q16"] = df["q16"].replace("満足", 5)
    df["q16"] = df["q16"].replace("概ね満足", 4)
    df["q16"] = df["q16"].replace("どちらともいえない", 3)
    df["q16"] = df["q16"].replace("やや不満", 2)
    df["q16"] = df["q16"].replace("不満", 1)
    df["q16"] = df["q16"].replace("満足した", 6)
    df["q16"] = df["q16"].replace("概ね満足した", 5)
    df["q16"] = df["q16"].replace("あまり満足しなかった", 4)
    df["q16"] = df["q16"].replace("満足しなかった", 2)
    df["q16"] = df["q16"].replace("オンライン授業は実施されなかった", 1)

    return df

# 集計する関数
async def aggregation( df_data, subject_id : str, question_number : str ):

    # データの取得
    df = await normal_replace(df_data)

    # 対象科目で絞り込み
    df_lec = df[df["course_id"] == subject_id]

    # 集計
    if df["title"][0] == "実験・実習":
        if question_number == "q10" or "q14":
            nums = [6, 5, 4, 3, 2, 1]
        else:
            nums = [5, 4, 3, 2, 1]
    elif df["title"][0] == "対面・オンライン":
        if question_number == "q8" or "q9" or "q10" or "q14" or "q16":
            nums = [6, 5, 4, 3, 2, 1]
        else:
            nums = [5, 4, 3, 2, 1]

    nums.sort(reverse=True)
    answer = []

    # 各ユニークな値の出現回数をカウント
    for num in nums:
        ans_count = df_lec[df_lec[question_number] == num].shape[0]
        if ans_count == 0:
            answer.append(0)
        else:
            answer.append(ans_count)

    return answer

# 順位を出す関数
async def normal_rank(df_data, subject_id, question_number : str ):
    # データの取得
    df = await normal_replace(df_data)

    # 科目を共通教育と専門で分類
    course_id_common = []
    course_id_expert = []
    for i in range(len(df["course_id"])):
        # ！情報システム工学科以外の科目も増えた場合は学科を増やす必要あり！
        if df["target_department"][i] == "理工学部 情報ｼｽﾃﾑ工学科":
            course_id_expert.append(df["course_id"][i])
        elif df["target_department"][i] == "理工学部":
            course_id_common.append(df["course_id"][i])

    # コースIDを取得
    target_row = df[df["course_id"] == subject_id].iloc[0]

    if target_row["target_department"] == "理工学部 情報ｼｽﾃﾑ工学科":
        course_ids = list(set(course_id_expert))
    elif target_row["target_department"] == "理工学部":
        course_ids = list(set(course_id_common))

    # 回答の平均の取得
    answer_aves = []
    for course_id in course_ids:
        # 回答を取得
        answer = await aggregation(df_data, course_id, question_number)

        # 平均を計算
        answer_ave = sum(answer) / len(answer)
        answer_aves.append(answer_ave)

    # DataFrameに格納
    answers = pd.DataFrame({"course_id": course_ids, "answer_average": answer_aves})

    # 順位を計算
    answers_rank = answers["answer_average"].rank(ascending=False).to_frame("rank")
    df_rank = pd.concat([answers, answers_rank], axis=1)
    rank = df_rank[df_rank["course_id"] == subject_id]["rank"].item()

    return rank


# 割合を算出する関数
async def normal_rate(df_data, subject_id, question_number : str ):
    # データの取得
    df = await normal_replace(df_data)

    # 対象科目で絞り込み
    df_lec = df[df["course_id"] == subject_id]

    # 回答数の取得
    num_df_lec = df_lec[question_number].dropna()
    ans_num = num_df_lec.shape[0]

    # 割合の算出
    answer = await aggregation(df_data, subject_id, question_number)
    ans_rate = []

    # 項目数の取得
    ans_index = await index(df_data, subject_id, question_number)

    for i in range(len(ans_index)):
        rate = answer[i] / ans_num * 100
        ans_rate.append(rate)

    return ans_rate

# 基本統計量の算出
async def normal_describe(df_data, subject_id):
    # データの取得
    df_ = await normal_replace(df_data)
    df = df_[df_["course_id"] == subject_id].reset_index()

    if df["title"][0] == "実験・実習":
        df_d = df[["q4", "q5", "q6", "q8", "q9", "q10", "q11", "q12", "q13", "q14", "q15", "q16"]]
    elif df["title"][0] == "対面・オンライン":
        df_d = df[["q4", "q5", "q6", "q7", "q8", "q9", "q10", "q11", "q12", "q13", "q14", "q15", "q16"]]
        df_d["q7"] = df_d["q7"].astype('float64')

    df_describe = df_d.describe()

    json_describe = {col: df_describe[col].tolist() for col in df_describe.columns if col in df_d.columns}

    return json_describe



# dfを取ってくる関数
async def get_json(df_data, subject_id):
    # データの取得
    df = await normal_replace(df_data)

    # 対象科目で絞り込み
    df_lec = df[df["course_id"] == subject_id].reset_index()

    # 科目コードを指定
    lecture_name = df_lec["course_id"][0]

    # 実験科目と通常科目で場合分け
    if df_lec["title"][0] == "実験・実習":
        # 項目名を取得
        q4_index = await index(df_data, lecture_name, "q4")
        q5_index = await index(df_data, lecture_name, "q5")
        q6_index = await index(df_data, lecture_name, "q6")
        q8_index = await index(df_data, lecture_name, "q8")
        q9_index = await index(df_data, lecture_name, "q9")
        q10_index = await index(df_data, lecture_name, "q10")
        q11_index = await index(df_data, lecture_name, "q11")
        q12_index = await index(df_data, lecture_name, "q12")
        q13_index = await index(df_data, lecture_name, "q13")
        q14_index = await index(df_data, lecture_name, "q14")
        q15_index = await index(df_data, lecture_name, "q15")
        q16_index = await index(df_data, lecture_name, "q16")

        # 割合を取得
        q4_ans = await normal_rate(df_data, lecture_name, "q4")
        q5_ans = await normal_rate(df_data, lecture_name, "q5")
        q6_ans = await normal_rate(df_data, lecture_name, "q6")
        q8_ans = await normal_rate(df_data, lecture_name, "q8")
        q9_ans = await normal_rate(df_data, lecture_name, "q9")
        q10_ans = await normal_rate(df_data, lecture_name, "q10")
        q11_ans = await normal_rate(df_data, lecture_name, "q11")
        q12_ans = await normal_rate(df_data, lecture_name, "q12")
        q13_ans = await normal_rate(df_data, lecture_name, "q13")
        q14_ans = await normal_rate(df_data, lecture_name, "q14")
        q15_ans = await normal_rate(df_data, lecture_name, "q15")
        q16_ans = await normal_rate(df_data, lecture_name, "q16")

        # 順位の取得
        question_num = ["q4", "q5", "q6", "q8", "q9", "q10", "q11", "q12", "q13", "q14", "q15", "q16"]
        ranks = []
        for question in question_num:
            rank = await normal_rank(df_data, lecture_name, question)
            ranks.append(rank)

        # flag作成
        flag = 1

        # df作成
        json_normal = {"q4_項目":q4_index, "q4_割合":q4_ans, "q5_項目":q5_index, "q5_割合":q5_ans,
                       "q6_項目":q6_index, "q6_割合":q6_ans, "q8_項目":q8_index, "q8_割合":q8_ans,
                       "q9_項目":q9_index, "q9_割合":q9_ans, "q10_項目":q10_index, "q10_割合":q10_ans,
                       "q11_項目":q11_index, "q11_割合":q11_ans, "q12_項目":q12_index, "q12_割合":q12_ans,
                       "q13_項目":q13_index, "q13_割合":q13_ans, "q14_項目":q14_index, "q14_割合":q14_ans,
                       "q15_項目":q15_index, "q15_割合":q15_ans, "q16_項目":q16_index, "q16_割合":q16_ans, "順位":ranks, "Flag":flag}

    elif df_lec["title"][0] == "対面・オンライン":
        # 項目名を取得
        q4_index = await index(df_data, lecture_name, "q4")
        q5_index = await index(df_data, lecture_name, "q5")
        q6_index = await index(df_data, lecture_name, "q6")
        q7_index = await index(df_data, lecture_name, "q7")
        q8_index = await index(df_data, lecture_name, "q8")
        q9_index = await index(df_data, lecture_name, "q9")
        q10_index = await index(df_data, lecture_name, "q10")
        q11_index = await index(df_data, lecture_name, "q11")
        q12_index = await index(df_data, lecture_name, "q12")
        q13_index = await index(df_data, lecture_name, "q13")
        q14_index = await index(df_data, lecture_name, "q14")
        q15_index = await index(df_data, lecture_name, "q15")
        q16_index = await index(df_data, lecture_name, "q16")

        # 割合を取得
        q4_ans = await normal_rate(df_data, lecture_name, "q4")
        q5_ans = await normal_rate(df_data, lecture_name, "q5")
        q6_ans = await normal_rate(df_data, lecture_name, "q6")
        q7_ans = await normal_rate(df_data, lecture_name, "q7")
        q8_ans = await normal_rate(df_data, lecture_name, "q8")
        q9_ans = await normal_rate(df_data, lecture_name, "q9")
        q10_ans = await normal_rate(df_data, lecture_name, "q10")
        q11_ans = await normal_rate(df_data, lecture_name, "q11")
        q12_ans = await normal_rate(df_data, lecture_name, "q12")
        q13_ans = await normal_rate(df_data, lecture_name, "q13")
        q14_ans = await normal_rate(df_data, lecture_name, "q14")
        q15_ans = await normal_rate(df_data, lecture_name, "q15")
        q16_ans = await normal_rate(df_data, lecture_name, "q16")

        # 順位の取得
        question_num = ["q4", "q5", "q6", "q7", "q8", "q9", "q10", "q11", "q12", "q13", "q14", "q15", "q16"]
        ranks = []
        for question in question_num:
            rank = await normal_rank(df_data, lecture_name, question)
            ranks.append(rank)

        # flag作成
        flag = 0

        # df作成
        json_normal = {"q4_項目": q4_index, "q4_割合": q4_ans, "q5_項目": q5_index, "q5_割合": q5_ans,
                       "q6_項目": q6_index, "q6_割合": q6_ans, "q7_項目": q7_index, "q7_割合": q7_ans, "q8_項目": q8_index, "q8_割合": q8_ans,
                       "q9_項目": q9_index, "q9_割合": q9_ans, "q10_項目": q10_index, "q10_割合": q10_ans,
                       "q11_項目": q11_index, "q11_割合": q11_ans, "q12_項目": q12_index, "q12_割合": q12_ans,
                       "q13_項目": q13_index, "q13_割合": q13_ans, "q14_項目": q14_index, "q14_割合": q14_ans,
                       "q15_項目": q15_index, "q15_割合": q15_ans, "q16_項目": q16_index, "q16_割合": q16_ans,
                       "順位":ranks, "Flag": flag}

    return json_normal