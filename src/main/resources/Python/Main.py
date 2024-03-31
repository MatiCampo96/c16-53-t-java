import subprocess

def reset_database():
    response = input("Si desea trabajar con los valores de prueba sugeridos, se le recomienda limpiar la base de datos 'rescatame'. ¿Desea limpiar la base de datos 'rescatame'? (S por sí, N por no): ")
    if response.lower() == 's':
        subprocess.run(['python', 'ResetDB.py'])

def main():
    reset_database()
    subprocess.run(['python', 'UsersDePrueba.py'])
    subprocess.run(['python', 'MascotasDePrueba.py'])
    subprocess.run(['python', 'SolicitudesDePrueba.py'])

if __name__ == "__main__":
    main()
