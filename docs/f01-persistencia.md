# F-01: Persistencia

## Entidades

- `Afiliado`: `id`, `numero_identificacion`, `nombres`, `apellidos`, `celular`, `correo`, `direccion`.
- `Mascota`: `id`, `nombre`, `especie`, `raza`, `edad`, `color`, `afiliado_id`.

## Relación

Un afiliado puede tener varias mascotas. Cada mascota debe referenciar un afiliado existente mediante `mascota.afiliado_id`.

No se utilizan entidades intermedias ni eliminaciones en cascada.

## Migración

La migración `V1__create_afiliado_mascota.sql` crea las tablas y la relación. La migración `V2__add_affiliate_contact_and_pet_details.sql` añade los datos de contacto y los detalles de mascota sin borrar ni reescribir filas existentes. Esas columnas nuevas son nullable para conservar registros anteriores; la API exige los campos al crear nuevos registros. La edad aceptada por la API y por la restricción de base de datos es un entero entre 0 y 100.

Hibernate no crea ni modifica el esquema: `spring.jpa.hibernate.ddl-auto` está configurado como `none`.

## Ejecución

1. Iniciar PostgreSQL:

   ```powershell
   docker compose up -d
   ```

2. Ejecutar el backend:

   ```powershell
   Push-Location backend
   mvn spring-boot:run
   Pop-Location
   ```

Flyway ejecuta automáticamente las migraciones pendientes al iniciar Spring Boot.

## Verificación

Con PostgreSQL disponible, las tablas pueden verificarse con:

```sql
\dt
\d afiliado
\d mascota
```

Las pruebas `PersistenciaF01Test` comprueban el guardado del afiliado, el guardado de la mascota asociada y las restricciones de afiliado obligatorio y número de identificación único.

## API de F-01

### Registrar afiliado

```http
POST /api/v1/afiliados
Content-Type: application/json
```

Request:

```json
{
   "numeroIdentificacion": "100",
   "nombres": "Ana",
   "apellidos": "Gomez",
   "celular": "3001234567",
   "correo": "ana@example.com",
   "direccion": "Calle 1"
}
```

El request contiene únicamente datos del afiliado. No se crea una mascota automáticamente.

Respuesta `201 Created`:

```json
{
   "id": 1,
   "numeroIdentificacion": "100",
   "nombres": "Ana",
   "apellidos": "Gomez",
   "celular": "3001234567",
   "correo": "ana@example.com",
   "direccion": "Calle 1"
}
```

### Agregar mascota a un afiliado

```http
POST /api/v1/afiliados/{afiliadoId}/mascotas
Content-Type: application/json
```

Request:

```json
{
   "nombre": "Rocky",
   "especie": "Perro",
   "raza": "Labrador",
   "edad": 3,
   "color": "Café"
}
```

La especie debe ser una de: Perro, Gato, Ave, Conejo, Hámster, Pez, Tortuga u Otro. La edad es entera y va de 0 a 100. La mascota se guarda con `afiliado_id` del path.

Respuesta `201 Created`:

```json
{
   "id": 1,
   "afiliadoId": 1,
   "nombre": "Rocky",
   "especie": "Perro",
   "raza": "Labrador",
   "edad": 3,
   "color": "Café"
}
```

### Validaciones y errores

- `400 Bad Request`: campos obligatorios, correo/formato, especie o edad inválidos.
- `404 Not Found`: afiliado inexistente al agregar una mascota.
- `409 Conflict`: número de identificación duplicado o conflicto de integridad.
- `500 Internal Server Error`: error inesperado sin exposición del stack trace.

Los errores se devuelven con `status`, `message` y `errors`.

### Flujo transaccional

`AfiliadoController` recibe y valida `CrearAfiliadoRequest`; `AfiliadoService` verifica duplicados y persiste el afiliado en una transacción independiente. La mascota se crea después mediante el endpoint anidado; `MascotaService` verifica que exista el afiliado y guarda la mascota relacionada dentro de su transacción.