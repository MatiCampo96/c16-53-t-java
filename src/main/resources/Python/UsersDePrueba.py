import requests
import pymysql

# Datos de conexión a la base de datos
DB_HOST = 'localhost'
DB_USER = 'root'
DB_PASSWORD = '123456'
DB_NAME = 'rescatame'

# Función para probar la conexión a la base de datos
def test_database_connection():
    try:
        conn = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, database=DB_NAME)
        print("Conexión exitosa a la base de datos")
        conn.close()
    except pymysql.Error as e:
        print(f"Error al conectar a la base de datos: {e}")

import requests

# Endpoint para agregar usuarios
API_URL = 'http://localhost:8080/registro'

# Función para agregar un usuario mediante el endpoint
def add_user(name, surname, email, birthdate, password, password2):
    data = {
        "name": name,
        "surname": surname,
        "email": email,
        "birthdate": birthdate,
        "password": password,
        "password2": password2
    }
    try:
        response = requests.post(API_URL, data=data)
        if response.status_code == 200:
            print(f"Usuario {email} agregado exitosamente")
        else:
            print(f"Error al agregar usuario {email}: {response.text}")
    except requests.RequestException as e:
        print(f"Error al conectar con el servidor: {e}")

if __name__ == "__main__":
    # Agregar usuarios de ejemplo
    print("Agregando usuarios...")
    users = [
        {"name": "Admin", "surname": "Istrador", "email": "admin@example.com", "birthdate": "1990-01-01", "password": "admin123", "password2": "admin123"},
        {"name": "María", "surname": "González", "email": "maria@example.com", "birthdate": "1985-05-05", "password": "password456", "password2": "password456"},
        {"name": "Juan", "surname": "Pérez", "email": "juan@example.com", "birthdate": "1990-01-01", "password": "password123", "password2": "password123"},
        {"name": "Julieta", "surname": "Garcia", "email": "Julieta@example.com", "birthdate": "1985-05-05", "password": "password456", "password2": "password456"},
        {"name": "Cholo", "surname": "Martinez", "email": "cholo@example.com", "birthdate": "1990-01-01", "password": "password123", "password2": "password123"}

    ]
    for user in users:
        add_user(**user)