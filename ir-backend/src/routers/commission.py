from fastapi import APIRouter, Depends
import pandas as pd
import json
import numpy as np
from src.datasource import irweb_data

router = APIRouter()

#grade_newテーブルから学年、GPA,学科名、クラスを取得する関数
async def get_gpa(db_connection=Depends(irweb_data)):
    df = await db_connection.query('grade_new', 'grade', 'department_name', 'gpa', 'class','available_year')
    df['gpa'] = df['gpa'].astype(float)
    df = df[df['available_year']==2022]
    return df

#GPAの分布を0.5ごとに分ける関数
def get_data(df):
    df = df['gpa']
    bins = np.linspace(0, 4, 9)
    cut = df.value_counts(bins=bins, sort=False)
    cut = cut.tolist()
    max_gpa_count = df[df >= 4.0].sum()
    cut.append(max_gpa_count)
    return cut
#データテーブルからGPAに関する基本統計量を取得する関数
async def get_gpa_toukeiryou(df):
    list = []
    list.append(df.shape[0])
    list.append(round(df.loc[:, 'gpa'].mean(), 4))
    list.append(df.loc[:, 'gpa'].median())
    list.append(df.loc[:, 'gpa'].min())
    list.append(df.loc[:, 'gpa'].max())
    list.append(round(df.loc[:, 'gpa'].std(), 4))
    return list

#学科別(1年生はクラス別)のGPAの情報を取得するAPI
@router.get("/grade/gpa_graph/{school_year}")
async def gpa_graph(school_year:str,db_connection=Depends(irweb_data)):
    df = await db_connection.query("grade_new", 'department_name', 'gpa', 'class','available_year',where_and={"grade":school_year})
    df = df[df['available_year']==2022]
    list_all = []

    if(school_year == 'B1'):
        list = get_data(df)
        list.insert(0, "全体")
        list_all.append(list)

        list = get_data(df[df['class'] == '1A'])
        list.insert(0, "Aクラス")
        list_all.append(list)

        list = get_data(df[df['class'] == '1B'])
        list.insert(0, "Bクラス")
        list_all.append(list)

        list = get_data(df[df['class'] == '1C'])
        list.insert(0, "Cクラス")
        list_all.append(list)

        list = get_data(df[df['class'] == '1D'])
        list.insert(0, "Dクラス")
        list_all.append(list)

    elif school_year == 'B':
        df = await get_gpa(db_connection)
        list = get_data(df)
        list.insert(0, "全体")
        list_all.append(list)

        list = get_data(df[df['department_name'] == '応用化学生物学科'])
        list.insert(0, "応用化学生物学科")
        list_all.append(list)

        list = get_data(df[df['department_name'] == '電子光工学科'])
        list.insert(0, "電子光工学科")
        list_all.append(list)

        list = get_data(df[df['department_name'] == '情報システム工学科'])
        list.insert(0, "情報システム工学科")
        list_all.append(list)

        list = get_data(df[df['grade'] == 'B1'])
        list.insert(0, "共通教育")
        list_all.append(list)

    else:
        list = get_data(df)
        list.insert(0, "全体")
        list_all.append(list)

        list = get_data(df[df['department_name'] == '応用化学生物学科'])
        list.insert(0, "応用化学生物学科")
        list_all.append(list)

        list = get_data(df[df['department_name'] == '電子光工学科'])
        list.insert(0, "電子光工学科")
        list_all.append(list)

        list = get_data(df[df['department_name'] == '情報システム工学科'])
        list.insert(0, "情報システム工学科")
        list_all.append(list)

    header = ['name', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i']
    df = pd.DataFrame(data=list_all, columns=header)
    print('type is ', type(json.loads(df.to_json(orient='records'))))
    return {"data": json.loads(df.to_json(orient='records'))}

#学科別(1年生はクラス別)に基本統計量を取得するAPI
@router.get("/grade/gpa_stat/{school_year}")
async def gpa_stat(school_year:str,db_connection=Depends(irweb_data)):
    df = await db_connection.query("grade_new", 'department_name', 'gpa', 'class','available_year', where_and={"grade": school_year})
    df = df[df['available_year']==2022]
    list_all = []

    if school_year == 'B1':
        list = await get_gpa_toukeiryou(df)
        list.insert(0, "全体")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['class'] == '1A'])
        list.insert(0, "Aクラス")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['class'] == '1B'])
        list.insert(0, "Bクラス")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['class'] == '1C'])
        list.insert(0, "Cクラス")
        list_all.append(list)

        list = await (get_gpa_toukeiryou(df[df['class'] == '1D']))
        list.insert(0, "Dクラス")
        list_all.append(list)

    elif school_year == 'B':
        df = await get_gpa(db_connection)
        list = await get_gpa_toukeiryou(df)
        list.insert(0, "全体")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['department_name'] == '応用化学生物学科'])
        list.insert(0, "応用化学生物学科")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['department_name'] == '電子光工学科'])
        list.insert(0, "電子光工学科")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['department_name'] == '情報システム工学科'])
        list.insert(0, "情報システム工学科")
        list_all.append(list)

        list = await (get_gpa_toukeiryou(df[df['grade'] == 'B1']))
        list.insert(0, "共通教育")
        list_all.append(list)

    else:
        list = await get_gpa_toukeiryou(df)
        list.insert(0, "全体")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['department_name'] == '応用化学生物学科'])
        list.insert(0, "応用化学生物学科")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['department_name'] == '電子光工学科'])
        list.insert(0, "電子光工学科")
        list_all.append(list)

        list = await get_gpa_toukeiryou(df[df['department_name'] == '情報システム工学科'])
        list.insert(0, "情報システム工学科")
        list_all.append(list)

    header = ['学科', '人数', '平均値', '中央値', '最小値', '最大値', '標準偏差']
    df = pd.DataFrame(data=list_all, columns=header)
    return {"data": json.loads(df.to_json(orient='records'))}

#教職課程に関する情報を取得するAPI
@router.get("/university/teacher_training/{academic_year}")
async def teacher_training(academic_year:int,db_connection=Depends(irweb_data)):

    df = await db_connection.query("teacher_training_course_number","*",where_and={"academic_year": academic_year})
    df = df.drop(columns=['subjects_taken_number','academic_year'])
    return {"data": json.loads(df.to_json(orient='records'))}

#大学院研究援助金に関する情報を取得するAPI
@router.get("/university/graduate_research_grants/{academic_year}")
async def teacher_training(academic_year:int,db_connection=Depends(irweb_data)):
    df = await db_connection.query("graduate_research_grants","*",where_and={"academic_year": academic_year})
    df = df.drop(columns=['academic_year'])
    return {"data": json.loads(df.to_json(orient='records'))}

#日本学生支援機構奨学金に関する情報を取得するAPI
@router.get("/university/JASSO_scholarship/{academic_year}")
async def teacher_training(academic_year:int,db_connection=Depends(irweb_data)):
    df = await db_connection.query("jasso_scholarship","*",where_and={"academic_year": academic_year})
    df = df.drop(columns=['academic_year'])
    return {"data": json.loads(df.to_json(orient='records'))}

#その他の奨学金に関する情報を取得するAPI
@router.get("/university/other_scholarship/{academic_year}")
async def teacher_training(academic_year:int,db_connection=Depends(irweb_data)):
    df = await db_connection.query("other_scholarship","*",where_and={"academic_year": academic_year})
    df = df.drop(columns=['academic_year'])
    return {"data": json.loads(df.to_json(orient='records'))}

#卒業に必要な最低単位数（共通教育科目)に関する情報を取得するAPI
@router.get("/university/required_credits_common_courses/{academic_year}")
async def teacher_training(academic_year:int,db_connection=Depends(irweb_data)):
    df = await db_connection.query("required_credits_common_courses","*",where_and={"academic_year": academic_year})
    df = df.drop(columns=['academic_year'])
    return {"data": json.loads(df.to_json(orient='records'))}

#卒業に必要な最低単位数（専門教育科目・その他)に関する情報を取得するAPI
@router.get("/university/graduation_minimum_credits_specialty_other/{academic_year}")
async def teacher_training(academic_year:int,db_connection=Depends(irweb_data)):
    df = await db_connection.query("graduation_minimum_credits_specialty_other","*",where_and={"academic_year": academic_year})
    df = df.drop(columns=['academic_year'])
    return {"data": json.loads(df.to_json(orient='records'))}