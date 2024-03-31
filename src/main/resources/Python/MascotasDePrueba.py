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

# Endpoint para agregar mascotas
API_URL = 'http://localhost:8080/createAnimal'

# Función para agregar una mascota mediante el endpoint
def add_pet(pet_name, breed, age_months, color, specie, size, description, image_path):
    data = {
        "pet_name": pet_name,
        "breed": breed,
        "age_months": age_months,
        "color": color,
        "specie": specie,
        "size": size,
        "description": description
    }
    files = {'image': open(image_path, 'rb')}
    try:
        response = requests.post(API_URL, data=data, files=files)
        if response.status_code == 200:
            print(f"Mascota {pet_name} agregada exitosamente")
        else:
            print(f"Error al agregar mascota {pet_name}: {response.text}")
    except requests.RequestException as e:
        print(f"Error al conectar con el servidor: {e}")

if __name__ == "__main__":
    # Agregar mascotas de ejemplo
    pets = [
        {"pet_name": "Marioos", "breed": "masrios", "age_months": "22", "color": "mer", "specie": "dog", "size": "small", "description": "Mongo", "image_path": "labrador12.jpeg"},
        # Agrega más mascotas aquí
    ]
    for pet in pets:
        add_pet(**pet)