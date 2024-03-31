import requests

def obtener_usuarios():

    # URL del endpoint
    url = 'http://localhost:8080/myUsers'

    # Realizar la solicitud GET al endpoint
    response = requests.get(url)

    # Verificar si la solicitud fue exitosa (código de respuesta 200)
    if response.status_code == 200:
        # Obtener el JSON de la respuesta
        data = response.json()
        
        # Inicializar listas para almacenar el email y el user_id de los registros
        emails = []
        user_ids = []
        
        # Iterar sobre los usuarios en la respuesta JSON
        for user in data:
            # Verificar si el rol no es ADMIN
            if user['role'] != 'ADMIN':
                # Agregar el email y el user_id a las listas correspondientes
                emails.append(user['email'])
                user_ids.append(user['user_id'])
        
        # Imprimir los emails y user_ids de los registros que no tienen rol ADMIN
        print("Emails de usuarios sin rol ADMIN:")
        for email in emails:
            print(email)
        
        return user_ids
    else:
        # Imprimir un mensaje de error si la solicitud no fue exitosa
        print(f"Error al realizar la solicitud: {response.status_code}")

def obtener_mascotas():

    # URL del endpoint
    url = 'http://localhost:8080/api/pets'

    # Realizar la solicitud GET al endpoint
    response = requests.get(url)

    # Verificar si la solicitud fue exitosa (código de respuesta 200)
    if response.status_code == 200:
        # Obtener el JSON de la respuesta
        data = response.json()
        
        # Inicializar listas para almacenar el nombre y el pet_id de los registros
        names = []
        pets_ids = []
        
        # Iterar sobre los usuarios en la respuesta JSON
        for pet in data:
            # Agregar el nombre y el pet_id a las listas correspondientes
            names.append(pet['pet_name'])
            pets_ids.append(pet['pet_id'])
        
        # Imprimir los emails y user_ids de los registros que no tienen rol ADMIN
        print("Nombres de las mascotas:")
        for name in names:
            print(name)
        
        return pets_ids
    else:
        # Imprimir un mensaje de error si la solicitud no fue exitosa
        print(f"Error al realizar la solicitud: {response.status_code}")
    

def add_request(user_id, pet_id, salary, sterilization_commitment, housing_type):
    # URL del endpoint
    url = 'http://localhost:8080/request'

    # Datos a enviar en el paquete
    payload = {
        'user_id': user_id,
        'pet_id': pet_id,
        'salary': salary,
        'sterilizationCommitment': sterilization_commitment,
        'housingType': housing_type
    }

    # Realizar la solicitud POST al endpoint
    try:
        response = requests.post(url, data=payload)

        # Verificar si la solicitud fue exitosa (código de respuesta 200)
        if response.status_code == 200:
            print("Solicitud creada exitosamente.")
        else:
            print(f"Error al crear la solicitud: {response.status_code}")
    except requests.RequestException as e:
        print(f"Error al conectar con el servidor_ {e}")

if __name__ == "__main__":
    # Obtener los IDs de usuario y mascota
    user_ids = obtener_usuarios()
    pet_ids = obtener_mascotas()

    # Agregar solicitudes de ejemplo
    if user_ids and pet_ids:
        print("Agregando solicitudes...")
        solicitudes = [
            {"user_id": user_ids[0],"pet_id": pet_ids[0], "salary": "2000", "sterilization_commitment": "True", "housing_type": "CASA"},
            {"user_id": user_ids[0],"pet_id": pet_ids[1], "salary": "4000", "sterilization_commitment": "False", "housing_type": "FINCA"},
            {"user_id": user_ids[1],"pet_id": pet_ids[2], "salary": "5000", "sterilization_commitment": "False", "housing_type": "APARTAMENTO"},
            {"user_id": user_ids[1],"pet_id": pet_ids[3], "salary": "1000", "sterilization_commitment": "True", "housing_type": "CASA"},
            {"user_id": user_ids[2],"pet_id": pet_ids[4], "salary": "2500", "sterilization_commitment": "True", "housing_type": "CASA"},
            {"user_id": user_ids[2],"pet_id": pet_ids[5], "salary": "3000", "sterilization_commitment": "False", "housing_type": "FINCA"},
            {"user_id": user_ids[3],"pet_id": pet_ids[6], "salary": "2500", "sterilization_commitment": "True", "housing_type": "FINCA"},
            {"user_id": user_ids[3],"pet_id": pet_ids[7], "salary": "4000", "sterilization_commitment": "False", "housing_type": "APARTAMENTO"},
            {"user_id": user_ids[2],"pet_id": pet_ids[8], "salary": "7000", "sterilization_commitment": "True", "housing_type": "CASA"},
            {"user_id": user_ids[3],"pet_id": pet_ids[9], "salary": "900", "sterilization_commitment": "True", "housing_type": "APARTAMENTO"}
        ]
        for solicitud in solicitudes:
            add_request(**solicitud)
else:
    print("No se pudieron obtener los IDs de usuario y mascota.")
