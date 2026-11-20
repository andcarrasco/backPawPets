# backPawPets

API REST de **PawPets**, tienda de productos para mascotas (Desarrollo Full Stack II, DSY1104, Evaluación 3).
El frontend React está en [andcarrasco/frontPawPets](https://github.com/andcarrasco/frontPawPets).

## Tecnologías

Spring Boot 4.1.2 · Java 17 · Spring Data MongoDB (MongoDB Atlas) · Bean Validation · springdoc-openapi 3.2.0 (Swagger) · Lombok · Maven Wrapper.

```
src/main/java/cl/duoc/pawpets
├── PawpetsApplication.java   clase principal (@SpringBootApplication)
├── config/                   CORS
├── controller/               endpoints REST (@RestController)
├── exception/                excepciones y manejo global de errores (@RestControllerAdvice)
├── model/                    documentos de MongoDB (@Document)
├── repository/               acceso a datos (MongoRepository)
└── service/                  reglas de negocio (@Service)
```

## Ejecutar en local

Requisitos: JDK 17 o superior y la variable de entorno `MONGODB_URI` con la cadena de conexión de Atlas
(cada integrante recibe la suya por mensaje privado; **nunca se escribe en el código**).

```powershell
setx MONGODB_URI "mongodb+srv://usuario:clave@cluster0.wkdersi.mongodb.net/pawpets"   # una sola vez
# cerrar y abrir la terminal
.\mvnw.cmd spring-boot:run
```

La API queda en `http://localhost:8081`. El frontend (`npm run dev` en frontPawPets) reenvía `/api` a ese puerto.

- Swagger UI: http://localhost:8081/swagger-ui.html
- Contrato OpenAPI: http://localhost:8081/v3/api-docs

## Errores

Todas las respuestas de error tienen el mismo formato, armado por `GlobalExceptionHandler`:

```json
{ "estado": 404, "mensaje": "No existe el producto con id 99." }
```

| Excepción | Código |
| --- | --- |
| `RecursoNoEncontradoException` | 404 |
| `DatosInvalidosException` y errores de `@Valid` | 400 |
| `ConflictoException` | 409 |
| `NoAutorizadoException` | 401 |

## Variables de entorno

| Variable | Uso |
| --- | --- |
| `MONGODB_URI` | Cadena de conexión de MongoDB Atlas (obligatoria) |
| `PORT` | Puerto; Railway la define sola. En local se usa 8081 |
| `CORS_ORIGENES` | Opcional. Orígenes permitidos separados por coma (por defecto `localhost:5173` y el frontend de Railway) |

## Equipo

María Pía Sepúlveda · Andrés Carrasco · Luis Veas · Pablo Rojas
