from fastapi import APIRouter, Depends
from src.datasource import irweb_data
import json

router = APIRouter()

# 質問項目の取得
@router.get("/review/title/{subject_id}")
async def get_title(subject_id:str, db_connection=Depends(irweb_data)):
    df_data = await db_connection.query('review_new', "course_id", "lecture_name", "question_4", "question_5",
                                   "question_6", "question_7", "question_8", "question_9", "question_10",
                                   "question_11", "question_12", "question_13", "question_14", "question_15",
                                   "question_16", "question_17", "question_18", "question_19")
    from src.review_title import df_title
    df = await df_title(df_data, subject_id)
    return {"data":json.loads(df.to_json(orient='records'))}

# 通常回答のデータの取得
@router.get("/review/graph/{subject_id}")
async def get_normal(subject_id: str, db_connection=Depends(irweb_data)):
    df_data = await db_connection.query("review_new",
                                   "lecture_name", "lecture_teacher", "title", 'course_id',
                                   'answer_4_1', 'answer_5_1', 'answer_6_1', 'answer_7_1',
                                   'answer_8_1', 'answer_9_1', 'answer_10_1', 'answer_11_1',
                                   'answer_12_1', 'answer_13_1', 'answer_14_1', 'answer_15_1',
                                   'answer_16_1', 'answer_17_1', 'answer_18_1', 'answer_19_1', 'target_department')

    from src.review_normal import get_json
    data_normal = await get_json(df_data, subject_id)
    return {"data":data_normal}

# 記述回答のデータの取得
@router.get("/review/description/{subject_id}")
async def get_description(subject_id: str, db_connection=Depends(irweb_data)):
    df_data = await db_connection.query("review_new",
                                        "lecture_name", "lecture_teacher", "title", 'course_id',
                                        'answer_4_1', 'answer_5_1', 'answer_6_1', 'answer_7_1',
                                        'answer_8_1', 'answer_9_1', 'answer_10_1', 'answer_11_1',
                                        'answer_12_1', 'answer_13_1', 'answer_14_1', 'answer_15_1',
                                        'answer_16_1', 'answer_17_1', 'answer_18_1', 'answer_19_1')

    from src.review_description import get_json_description
    data_description = await get_json_description(df_data, subject_id)
    return {"data": data_description}

# 基本統計量の取得
@router.get("/review/describe/{subject_id}")
async def get_describe(subject_id: str, db_connection=Depends(irweb_data)):
    df_data = await db_connection.query("review_new",
                                        "lecture_name", "lecture_teacher", "title", 'course_id',
                                        'answer_4_1', 'answer_5_1', 'answer_6_1', 'answer_7_1',
                                        'answer_8_1', 'answer_9_1', 'answer_10_1', 'answer_11_1',
                                        'answer_12_1', 'answer_13_1', 'answer_14_1', 'answer_15_1',
                                        'answer_16_1', 'answer_17_1', 'answer_18_1', 'answer_19_1')

    from src.review_normal import normal_describe
    data_describe = await normal_describe(df_data, subject_id)
    return {"data": data_describe}