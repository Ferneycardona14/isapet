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

## Seguridad administrativa

La autenticación usa sesiones HTTP y contraseñas BCrypt. La migración Flyway V4 crea las tablas de usuarios y roles e inicializa únicamente el rol `ADMIN`; no contiene cuentas ni credenciales. El primer administrador solo se crea si se habilita explícitamente `BOOTSTRAP_ADMIN_ENABLED` y se proporcionan `BOOTSTRAP_ADMIN_USERNAME` y `BOOTSTRAP_ADMIN_PASSWORD` mediante el entorno o un gestor de secretos. El proceso no reemplaza ni promueve una cuenta existente. Después del bootstrap, deshabilita la opción y retira las credenciales del entorno.

La cookie de sesión es `HttpOnly`, `SameSite=Lax` y `Secure` por defecto. Para desarrollo exclusivamente local sobre HTTP se puede establecer `SESSION_COOKIE_SECURE=false`; en producción debe mantenerse habilitado y servirse la aplicación mediante HTTPS. `FRONTEND_ORIGIN` permite configurar un origen exacto para CORS cuando frontend y backend no comparten origen; no admite comodines con credenciales.

El cliente debe solicitar `GET /api/v1/auth/csrf` antes de iniciar sesión y enviar el valor de la cookie `XSRF-TOKEN` en `X-XSRF-TOKEN` en las operaciones que cambian estado. La autenticación se realiza con `POST /api/v1/auth/login` y JSON `{"username":"…","password":"…"}`; la sesión queda en la cookie `JSESSIONID`. Después del login, vuelve a solicitar el token CSRF antes de otras operaciones mutables. `GET /api/v1/auth/session` devuelve la identidad y roles de la sesión; `POST /api/v1/auth/logout` cierra la sesión y requiere CSRF.

Los prefijos `/api/v1/admin/**`, `/api/v1/afiliados/**` y `/api/v1/mascotas/**` requieren el rol `ADMIN`. La pantalla Angular de login integra la sesión y CSRF; las pantallas F-01 y la administración del catálogo consumen la API con la cookie de sesión. La ruta `/planes` está protegida también por el guard Angular, sin sustituir la autorización del backend. Las sesiones se guardan en memoria del servidor; se invalidan al reiniciar y no se comparten entre varias instancias.

La administración del catálogo está disponible en `/planes`. Permite consultar planes y su historial, crear planes con una primera versión, publicar versiones comerciales inmutables, consultar la versión aplicable a una fecha y desactivar planes sin eliminar su historial. No permite cancelar ni reemplazar versiones futuras; esa política sigue pendiente. Las operaciones utilizan `/api/v1/admin/planes/**` y requieren rol `ADMIN` y CSRF en solicitudes mutables.

- `GET /api/v1/admin/planes`: lista los planes y sus versiones.
- `GET /api/v1/admin/planes/{planId}`: consulta el detalle y el historial.
- `GET /api/v1/admin/planes/{planId}/version-aplicable?fecha=YYYY-MM-DD`: consulta la versión aplicable.
- `POST /api/v1/admin/planes`: crea un plan y su primera versión.
- `POST /api/v1/admin/planes/{planId}/versiones`: publica una nueva versión comercial.
- `PATCH /api/v1/admin/planes/{planId}/desactivar`: desactiva el plan conservando su historial.

Endpoints de administración:

- `GET /api/v1/afiliados?page=0&size=10&q=texto`: listado paginado y búsqueda por identificación, nombres, apellidos o correo. `q` es opcional.
- `GET /api/v1/afiliados/{id}`: detalle del afiliado.
- `PUT /api/v1/afiliados/{id}`: actualiza nombres, apellidos, celular, correo y dirección; la identificación no se modifica.
- `GET /api/v1/afiliados/{id}/mascotas`: lista las mascotas del afiliado.
- `POST /api/v1/afiliados/{id}/mascotas`: agrega mascota al afiliado.
- `PUT /api/v1/mascotas/{mascotaId}`: actualiza los datos de mascota sin cambiar el afiliado propietario.

Las pruebas backend se ejecutan con `mvn test` desde `backend`; las pruebas frontend con `npm test -- --watch=false --browsers=ChromeHeadless` desde `frontend`.