from fastapi import APIRouter, Depends
from src.datasource import irweb_data
import json

router = APIRouter()

@router.get("/questionnaire/top/grid")
async def questionnaire_top_grid(db_connection=Depends(irweb_data)):
    df = await db_connection.query_sql("SELECT DISTINCT lecture_name, available_year, target_grade, target_department, compulsory_subjects, number_credits_course FROM grade_new")
    return {"data": json.loads(df.to_json(orient="records"))}
