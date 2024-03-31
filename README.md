# c16-53-t-java

Proyecto Encuéntrame

Java version : 17 

# Setup

1. Install MySQL
2. Create database 

```
CREATE DATABASE rescatame;
```

3. To connect to the database you must define the following environment variables with your database username and password respectively:

```
MYSQL_DB_USERNAME
MYSQL_DB_PASSWORD
```

# Notas Matias
Ultimo commit tengo servicios y controllers de Article y Event, en adicion a mi version de CareGiver que pronto reemplazare con la de Juan, Aun no estan bien probado el CRUD de estas entidades porque no pude crear un Usuario para los articulos y eventos.
Estoy batallando con el MyUser, lo estoy editando para crear algo sencillo que pueda probar con el back, por mas que retroceda un poco en seguridad de momento

```
Cree un GetAllUsers y un GetAllPets que creo que me servira para crear mi RequestAdoption CRUD. Podria evaluar eliminarse estas modificaciones de users y pets, tuve que hacer leves modificaciones en las entidades.

Probablemente tenga que rehacer el CREATE del serviceIMPL si no logro adaptarlo, con ello el Controller tambien, creare un RestController que adaptare a controller luego cuando haya manera de manejar login y sesiones.
Hecho
```

Puedo crear la RequestAdoption, falta capturar la ID de la sesion, que el template se genere como plantilla a partir de ser derivado de una mascota, Ahi Ajusto el formulario para que funcione el metodo.
Correji el asunto de la creacion de imagen y mascota, falta manejar sus contingencias como que reemplaza una imagen previa con su mismo nombre y probablemente no sea facil de relacionarlo con las mascotas.

```
Funciona el login, 
para crear un admin se puede actualizar por mail a un usuario en workbenc
funciona la lista de solicitudes, las solicitudes que hay hasta el momento las cree por postman, la lista en el nav solo se le renderizal al admin. No estan tocados los permisos. Aun no esta completo el RequestMascota para el front, lo mas probable es que termine por desplegar un select para elegir mascotas de la base de datos

Hay una pagina de mascotas que es capaz de desplegar algunos atributos de las mismas y su imagen.
Estoy trabajando en recortar la imagen que se va a subir con Cropper JS (https://www.youtube.com/watch?v=xS2VrknVfzE), una vez que pueda estandarizar el tamaño de la imagen, continuare con el sitio de despliegue de mascotas. 

Dejo el standarizado de las imagenes y logica de front para otro momento, ya avance con la logica de despliegue de mascotas en frontend y solicitud de adopcion, tengo que modificar el acceso a los endpoints y la pagina de error para los usuarios.
Tendre que limpiar la carpeta de statics y dirigir las imagenes hacia img/pets. Tengo que preparar scripts para objetos de prueba para una base limpia

Funciona todo aunque es facil de romper, y faltan todas las restricciones de seguridad. Seguir con springSecurity. Listo el python para insertar datos de prueba
```



