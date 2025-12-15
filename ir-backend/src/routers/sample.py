from fastapi import APIRouter, Depends
import pandas as pd
import json

router = APIRouter()

@router.get("/sample/one")
async def sample_one():
    print("sample one start")
    data = [['Jan', 10], ['Feb', 15], ['March', 55], ['April', 70], ['May', 65], ['Jun', 72], ['Jul', 78]]
    header = ['month', 'score']
    df = pd.DataFrame(data=data, columns=header)
    print('type is ', type(json.loads(df.to_json(orient='records'))))
    return {"data": json.loads(df.to_json(orient='records'))}

@router.get("/sample/two")
async def sample_two():
    data = [['2022', 25.3, 14.7, 21.9, 12.1, 7.0, 19.0], ['2023', 31.2, 11.1, 19.6, 12.1, 7.0, 19.0]]


@router.get("/sample/two")
async def sample_two():
    data = [['2022', 10.1, 40.5, 31.7, 6.7, 5.0, 5.0], ['2021', 20.3, 10.5, 30.1, 10.5, 21.8, 11.1]]
    header = ['年度', '秀', '優', '良', '可', '不可', '欠席']
    df = pd.DataFrame(data=data, columns=header)
    print('type is ', type(json.loads(df.to_json(orient='records'))))
    return {"data": json.loads(df.to_json(orient='records'))}

@router.get("/sample/three")
async def sample_three():
    data = [['2022', 40.7, 3.9, 3.2, 3.5]]
    header = ['年度', '難易度', '学習量', '進行速度', '興味関心の向上']
    df = pd.DataFrame(data=data, columns=header)
    print('type is ', type(json.loads(df.to_json(orient='records'))))
    return {"data": json.loads(df.to_json(orient='records'))}


@router.get("/hello")
async def hello():
    data = [['Japan', 'ja'], ['America', 'eng'], ['England', 'eng']]
    header = ['国名', '言語']
    df = pd.DataFrame(data=data, columns=header)
    return {"data": json.loads(df.to_json(orient='records'))}


@router.get("/sample/four")
def samplefour():
    data = [['千歳　太郎', 3, 'AI', 'S'], ['科学　花子', 3, 'AI', 'A'], ['技術　次郎', 2, 'Java', 'D']]
    header = ['氏名', '学年', '科目', '成績']
    df = pd.DataFrame(data=data, columns=header)
    return {"data": json.loads(df.to_json(orient=('records')))}