# BrainyCar - Grupo A Todo Gas

## Método de compilación

### Archivo api_keys.xml

Dentro del apartado _values_ del proyecto, se debe crear un archivo api_keys.xml que contenga dos strings del tipo:
<string name="general_key">clave-de-desarrollador-de-android</string>
<string name="server_client_id">clave-de-claves-para-autenticación</string>

### Archivo atodogas.keystore

Almacenado directamente en el directorio raiz _app_.
Almacenará dentro las claves para la autenticación con Google

### Archivo signing.properties

Almacenado directamente en el directorio raíz _app_.
Será un archivo de propiedades que contendrá algo similar a:
STORE_FILE=nombre-del-almacen-de-claves
STORE_PASSWORD=clave-del-almacen
KEY_ALIAS=alias-clave
KEY_PASSWORD=contraseña

### Instalación

Una vez configurados todos los archivos, se debe realizar un build-clean para limpiar posibles configuraciones del proyecto, y realizar una nueva build a través de Android Studio.

### Ejecución

Al ejecutar la aplicación por primera vez, aparecerá una pantalla de selección de cuenta de Google con la que se podrá autenticar en la aplicación.
Una vez seleccionada, la aplicación llevará directamente a la actividad inicial donde se podrá navegar con normalidad a través de sus opciones.
