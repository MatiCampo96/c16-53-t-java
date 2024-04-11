import os
import requests
import pymysql

# Datos de conexión a la base de datos
DB_HOST = 'localhost'
DB_USER = os.getenv('MYSQL_DB_USERNAME')
DB_PASSWORD = os.getenv('MYSQL_DB_PASSWORD')
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
    print("Agregando mascotas...")
    pets = [
        {"pet_name": "Manchitas", "breed": "Pitbull", "age_months": "10", "color": "marron", "specie": "dog", "size": "large", "description": "Perro manso", "image_path": "pets/perro1.jpg"},
        {"pet_name": "Fred", "breed": "Caniche", "age_months": "2", "color": "rojo", "specie": "dog", "size": "small", "description": "Perro agresivo", "image_path": "pets/perro2.jpg"},
        {"pet_name": "Marcos", "breed": "Puddle", "age_months": "22", "color": "blanco", "specie": "dog", "size": "medium", "description": "Perro jugueton", "image_path": "pets/perro3.jpg"},
        {"pet_name": "Luna", "breed": "Labrador", "age_months": "5", "color": "azul", "specie": "dog", "size": "large", "description": "Perro protector", "image_path": "pets/perro4.jpg"},
        {"pet_name": "Tito", "breed": "Siberiano", "age_months": "7", "color": "Blanco y negro", "specie": "dog", "size": "large", "description": "Perro muy activo", "image_path": "pets/perro5.jpeg"},
        {"pet_name": "Kike", "breed": "Siames", "age_months": "10", "color": "gris", "specie": "cat", "size": "large", "description": "Gato agresivo", "image_path": "pets/gato1.jpg"},
        {"pet_name": "Michi", "breed": "Caracat", "age_months": "22", "color": "negro", "specie": "cat", "size": "small", "description": "Gato tranquilo", "image_path": "pets/gato2.jpg"},
        {"pet_name": "Mifusus", "breed": "Caracal", "age_months": "35", "color": "naranja", "specie": "cat", "size": "medium", "description": "Gato vicioso", "image_path": "pets/gato3.jpeg"},
        {"pet_name": "Nala", "breed": "Spinyx", "age_months": "10", "color": "marron", "specie": "cat", "size": "large", "description": "Gato compulsivo", "image_path": "pets/gato4.jpg"},
        {"pet_name": "Ruby", "breed": "Ruso azul", "age_months": "11", "color": "beige", "specie": "cat", "size": "small", "description": "GAto ludopata", "image_path": "pets/gato5.jpeg"}
    
    ]
    for pet in pets:
        add_pet(**pet)