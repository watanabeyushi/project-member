from fastapi import APIRouter, Depends
import pandas as pd
import json
from src.datasource import irweb_data, irweb_users
from collections import Counter

router = APIRouter()

# ここにhelloworld_grade関数を作成

@router.get("/grade/helloworld")
async def helloworld_grade(db_connection=Depends(irweb_data()
                                                 )):
    # データベースであるirweb-dataのgrade_newというテーブルからデータを取得
    # 取得する情報は成績、科⽬名、担当教員名
    df = await db_connection.query("grade_new", "grading", "lecture_name",
                                   "lecture_teacher",
                                   where_and={"course_id": 'jhPZad'})
    # 取得したデータの加⼯
    # Counterクラスを使ってカウント(importを忘れないように)
    count_dict = dict(Counter(df["grading"].values))
    count_dict["lecture_teacher"] = df["lecture_teacher"][0]
    count_dict["lecture_name"] = df["lecture_name"][0]
    print({"data": count_dict})
    return {"data": count_dict}
