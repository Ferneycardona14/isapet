# ISAPET

Sistema web para la gestión de afiliados de una entidad de salud para mascotas.

## Estructura

- `backend`: API REST con Spring Boot 3.4.5, Java 17 y Maven.
- `frontend`: aplicación Angular 18 y TypeScript.
- `docs`: documentación, trazabilidad y evidencias.
- `docker-compose.yml`: PostgreSQL para desarrollo local.

F-01 permite registrar afiliados y agregar cero o varias mascotas relacionadas con cada afiliado. La Gestión de Afiliados permite consultar, buscar, editar y administrar sus mascotas.

## PostgreSQL local

Requisitos: Docker Desktop iniciado.

Desde la raíz del repositorio, inicia PostgreSQL:

```powershell
docker compose up -d postgres
docker compose ps
```

La base de datos del proyecto está disponible en `localhost:5433` (el puerto interno del contenedor es `5432`). La configuración local usa la base `isapet` y el usuario de desarrollo definido en `docker-compose.yml`. El volumen `isapet-postgres-data` conserva los datos entre reinicios.

Flyway aplica las migraciones pendientes al iniciar el backend.

## Backend y pruebas

Requisitos: Java 17 y Maven.

Desde la raíz del repositorio, ejecuta el backend:

```powershell
Push-Location backend
mvn spring-boot:run
Pop-Location
```

Para ejecutar las pruebas:

```powershell
Push-Location backend
mvn test
Pop-Location
```

## Gestión de Afiliados

Con backend, PostgreSQL y frontend activos, abre `http://localhost:4200/afiliados` para listar y buscar afiliados. Desde el listado se puede abrir el detalle, editar datos de contacto y consultar/agregar/editar mascotas. El formulario de alta de afiliado continúa en `http://localhost:4200/afiliados/registro`.

Swagger/OpenAPI está disponible en `http://localhost:8080/swagger-ui/index.html`.

Endpoints de administración:

- `GET /api/v1/afiliados?page=0&size=10&q=texto`: listado paginado y búsqueda por identificación, nombres, apellidos o correo. `q` es opcional.
- `GET /api/v1/afiliados/{id}`: detalle del afiliado.
- `PUT /api/v1/afiliados/{id}`: actualiza nombres, apellidos, celular, correo y dirección; la identificación no se modifica.
- `GET /api/v1/afiliados/{id}/mascotas`: lista las mascotas del afiliado.
- `POST /api/v1/afiliados/{id}/mascotas`: agrega mascota al afiliado.
- `PUT /api/v1/mascotas/{mascotaId}`: actualiza los datos de mascota sin cambiar el afiliado propietario.

Las pruebas backend se ejecutan con `mvn test` desde `backend`; las pruebas frontend con `npm test -- --watch=false --browsers=ChromeHeadless` desde `frontend`.