from fastapi import FastAPI
from pydantic import BaseModel
import pandas as pd
from src.routers import commission
from src.routers import sample
from src.routers import student
from src.datasource import DbConnectionHolder
from src.routers import class_select, questionnaire, user_manage, helloworld
from src.routers.sample import hello

app = FastAPI()
app.include_router(sample.router)
app.include_router(class_select.router)
app.include_router(student.router)
app.include_router(commission.router)
app.include_router(questionnaire.router)
app.include_router(class_select.router)
app.include_router(user_manage.router)
app.include_router(helloworld.router)


class Hello(BaseModel):
    message: str

@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


@app.post("/hello")
async def say_hello(hello: Hello):
    return {"message": f"Hello {hello.message}"}

@app.get("/subject")
async def subject():
    df = pd.DataFrame([[1,2,3],[4,5,6],[7,8,9]])
    return df.to_parquet()

@app.on_event("startup")
async def startup():
    DbConnectionHolder.init()

@app.on_event("shutdown")
async def shutdown():
    DbConnectionHolder.close_all()
    print("Connection pool closed")


