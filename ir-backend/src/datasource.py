from psycopg2 import pool
import os
from dotenv import load_dotenv
from src.db_connection import DbConnection

load_dotenv()


class DbConnectionHolder:
    data_db_connection = None
    user_db_connection = None

    @classmethod
    def init(cls):
        cls.data_db_connection = DbConnection(pool.SimpleConnectionPool(1, 5, host=os.getenv("DB_HOST"),
                                                                        dbname=os.getenv("DB_NAME_DATA"),
                                                                        user=os.getenv("DB_USER"),
                                                                        password=os.getenv("DB_PASSWORD")))
        cls.user_db_connection = DbConnection(pool.SimpleConnectionPool(1, 5, host=os.getenv("DB_HOST"),
                                                                        dbname=os.getenv("DB_NAME_USER"),
                                                                        user=os.getenv("DB_USER"),
                                                                        password=os.getenv("DB_PASSWORD")))

    @classmethod
    def close_all(cls):
        cls.data_db_connection.connection_pool.closeall()
        cls.user_db_connection.connection_pool.closeall()


def irweb_data():
    return DbConnectionHolder.data_db_connection


def irweb_users():
    return DbConnectionHolder.user_db_connection
