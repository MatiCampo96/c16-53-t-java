import os
import mysql.connector

# Configuración de la conexión a la base de datos
host = "localhost"
user = "root"
password = "123456"
database_name = "rescatame"

# Lista de tablas en el orden en que se deben limpiar
tables_to_truncate = [
    "donaciones_historico",
    "article",
    "event_response",
    "event",
    "care_giver",
    "users",
    "image_data",
    "file_data",
    "request_adoption",
    "pet",
    "my_user"
]

def reset_database(host, user, password, database_name):
    try:
        # Conexión al servidor MySQL
        connection = mysql.connector.connect(
            host=host,
            user=user,
            password=password,
            database=database_name  # Conectarse a la base de datos específica
        )

        # Creación del cursor
        cursor = connection.cursor()

        #Desactivar verificación de claves foraneas durante la limpieza
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")


        # Truncar cada tabla en el orden especificado
        for table in tables_to_truncate:
            cursor.execute(f"TRUNCATE TABLE {table}")

        #Reactivar verificación de claves foraneas luego de la limpieza
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")

        print("Tablas limpiadas exitosamente.")

    except mysql.connector.Error as error:
        print(f"Error al limpiar las tablas: {error}")

    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()
            print("Conexión a MySQL cerrada.")

# Llamar a la función para limpiar las tablas
reset_database(host, user, password, database_name)


def delete_files_in_directory(directory):
    try:
        print("Eliminando imagenes de mascotas...")
        # Iterar sobre los archivos en el directorio
        for filename in os.listdir(directory):
            file_path = os.path.join(directory, filename)
            # Verificar si es un archivo y no un directorio
            if os.path.isfile(file_path):
                # Eliminar el archivo
                os.remove(file_path)
                print(f"Archivo {file_path} eliminado correctamente.")
    except Exception as error:
        print(f"Error al eliminar archivos en el directorio {directory}: {error}")

# Configuración del directorio a limpiar
directory_to_clean = "/home/matiasj/Documentos/No Country!/encuentrame/src/main/resources/static/img/pets"

# Llamada a la función para eliminar archivos en el directorio
delete_files_in_directory(directory_to_clean)
