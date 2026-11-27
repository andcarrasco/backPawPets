# backPawPets

API de la tienda PawPets para la asignatura Desarrollo Full Stack II (DSY1104) de Duoc UC. Está hecha con Spring Boot
y guarda los datos en MongoDB Atlas. Requiere un JDK 17 o superior y usa Maven Wrapper.

El frontend que consume esta API está en [frontPawPets](https://github.com/andcarrasco/frontPawPets).

## Ejecutar en Windows

La conexión a la base se lee de la variable de entorno `MONGODB_URI`, así que la clave no queda en el código. Cada
integrante tiene su propia cadena de conexión. Se define una sola vez y después hay que cerrar y abrir la terminal.

```powershell
setx MONGODB_URI "mongodb+srv://<usuario>:<clave>@cluster0.wkdersi.mongodb.net/pawpets"
```

Luego clona el repositorio e inicia la API. Espera el mensaje `Started PawpetsApplication`.

```powershell
git clone https://github.com/andcarrasco/backPawPets.git
cd backPawPets
.\mvnw.cmd spring-boot:run
```

La API queda en `http://localhost:8081`.

## Cargar los datos

La API no carga datos al arrancar. Los archivos de `src/main/resources/data` (`productos.json`, `categorias.json` y
`usuarios.json`) se importan una vez en la base `pawpets`, cada uno en la colección con su mismo nombre. Se puede
hacer con MongoDB Compass (**Add data → Import JSON or CSV file**) o desde el Data Explorer de Atlas.

Si se borra un producto directamente en la base, deja de aparecer en el catálogo del frontend.

## Explorar la API con Swagger

1. Abre http://localhost:8081/swagger-ui.html con el backend iniciado.
2. Despliega una ruta, por ejemplo **GET /api/productos**, y revisa la respuesta **200**.
3. Pulsa **Try it out** y luego **Execute** para ver el código HTTP y el JSON que devuelve la API.

El contrato OpenAPI está en http://localhost:8081/v3/api-docs.

## Rutas

- Productos: `GET /api/productos`, `GET /api/productos/{id}`, `POST`, `PUT` y `DELETE`.
- Categorías: `GET /api/categorias`, `GET /api/categorias/{id}`, `POST`, `PUT` y `DELETE`.
- Usuarios: `GET /api/usuarios`, `GET /api/usuarios/{id}`, `POST`, `PUT` y `DELETE`.
- Login: `POST /api/auth/login` con el correo y la clave.
- Pedidos: `GET /api/pedidos`, `GET /api/pedidos/{id}` y `POST /api/pedidos`. Al crear un pedido se revisa y se
  descuenta el stock de cada producto.
- Reportes: `GET /api/reportes/ventas?mes=AAAA-MM` devuelve el total vendido, la cantidad de pedidos y los cinco
  productos más vendidos del mes.

También puedes probar la API desde PowerShell.

```powershell
Invoke-RestMethod http://localhost:8081/api/productos
```

## Pruebas

Las pruebas unitarias de los servicios usan JUnit 5 y Mockito, así que no necesitan conexión a la base.

```powershell
.\mvnw.cmd test
```

En la carpeta `postman` está la colección `PawPets.postman_collection.json`. Con la API iniciada, se importa en
Postman y se ejecuta con **Run collection**. Recorre el CRUD de cada recurso y revisa los errores 400, 401, 404 y 409.

## Errores

Cuando algo falla, la API responde con un JSON que tiene el código y un mensaje, por ejemplo:

```json
{ "estado": 404, "mensaje": "No existe el producto con id 99." }
```

Ese formato lo arma `GlobalExceptionHandler`. Un dato que no existe devuelve 404, un dato inválido o que no pasa
`@Valid` devuelve 400, un correo repetido, una categoría que todavía tiene productos o un pedido sin stock suficiente devuelve 409, y un login incorrecto devuelve 401.

## Organización

- `controller`: `ProductosController`, `CategoriasController`, `UsuariosController`, `AuthController`,
  `PedidosController` y `ReportesController` exponen las rutas HTTP y las documentan para Swagger.
- `service`: las clases `*Service` tienen las reglas de cada recurso, como el correo único o el descuento de stock.
- `repository`: `RepositorioProductos` y los demás repositorios acceden a MongoDB con Spring Data.
- `model`: los documentos que se guardan en la base y los datos que llegan en las peticiones.
- `exception`: las excepciones propias y el manejo global de errores.
- `config`: CORS, para que el frontend local y el publicado puedan llamar a la API.

## Railway

En Railway el puerto lo entrega la variable `PORT` y la conexión se define en `MONGODB_URI`. Si el frontend se publica
en otra dirección, se agrega en `CORS_ORIGENES` (separando por comas). Por defecto se permiten
`http://localhost:5173` y el sitio publicado del frontend.

Para generar el JAR en `target/`, ejecuta `.\mvnw.cmd clean package`.

## Integrantes

María Pía Sepúlveda, Andrés Carrasco, Luis Veas y Pablo Rojas.
