from fastapi import APIRouter, Depends
from src.datasource import irweb_users
import json

router = APIRouter()

@router.get("/users/grid")
async def questionnaire_top_grid(db_connection=Depends(irweb_users)):
    df = await db_connection.query_sql("SELECT u.id, u.login_id, u.user_name, u.password, u.is_available, r.display_name FROM users AS u INNER JOIN  user_role AS ur ON u.id = ur.user_id INNER JOIN role r ON ur.role_id = r.id")
    return {"data": json.loads(df.to_json(orient="records"))}