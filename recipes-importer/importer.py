import mysql.connector
import pandas as pd
import uuid
from text import TextColor as tc

def connect_mysql(host, user, password, database):
    db = mysql.connector.connect(
        host=host,
        user=user,
        password=password,
        database=database
    )
    return db

def insert_recipe(db, name, ingredients, steps, loves, description, image):
    cursor = db.cursor()
    sql = "INSERT INTO recipes (id, name, ingredients, steps, loves, description, image, createdAt, updatedAt) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)"
    id = str(uuid.uuid4())
    created_at = pd.Timestamp.now()
    val = (id, name, ingredients, steps, loves, description, image, created_at, created_at)
    cursor.execute(sql, val)
    db.commit()

HOST = "localhost"
USER = "root"
PASSWORD = ""
DATABASE = "euy_db"
EXCEL_FILE = "data.xlsx"

if __name__ == "__main__":
    try:
        db = connect_mysql(
            host=HOST,
            user=USER,
            password=PASSWORD,
            database=DATABASE
        )
        print(f"{tc.GREEN}Success: Connected to database{tc.END}")
    except:
        print(f"{tc.RED}Error: Can't connect to database{tc.END}")
        exit(1)

    df = pd.read_csv("recipes-dataset.csv")
    recipes = tuple(df[['name', 'ingredients', 'steps', 'loves', 'description', 'image']].values)
    for name, ingredients, steps, loves, description, image in recipes:
        try:
            if (not isinstance(description, str)):
                description = ""
            if (not isinstance(image, str)):
                image = ""

            print(f"{tc.YELLOW}Action: Inserting {name}{tc.END}")
            insert_recipe(db, name, ingredients, steps, loves, description, image)
            print(f"{tc.GREEN}Success: Inserting {name}{tc.END}")
        except Exception as e:
            print(f"{tc.RED}Error: Inserting {name}{tc.END}")
            print(f"{tc.RED}Error: {e}{tc.END}")
            continue
        