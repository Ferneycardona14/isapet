# Trazabilidad: Gestión de Afiliados

## Alcance

Administrar los datos de afiliados y la relación existente Afiliado 1:N Mascota. Esta entrega no implementa planes, bonos, prestaciones ni autenticación.

## Trazabilidad técnica

```text
Gestión de Afiliados
  -> F-01: registrar afiliado y asociar cero o varias mascotas
  -> Casos: listar/buscar, consultar detalle, editar afiliado, listar/agregar/editar mascota
  -> REST: GET/PUT /api/v1/afiliados, GET /api/v1/afiliados/{id},
           GET/POST /api/v1/afiliados/{id}/mascotas, PUT /api/v1/mascotas/{mascotaId}
  -> Angular: lista-afiliados, detalle-afiliado, editar-afiliado, registro-afiliado
  -> Persistencia: AfiliadoRepository, MascotaRepository, PostgreSQL; Flyway V1/V2
  -> Pruebas: controller, service, persistencia, componentes y pruebas E2E manuales
```

## Componentes relacionados

- API: `AfiliadoController`, `MascotaController`.
- Aplicación: `AfiliadoService`, `MascotaService`.
- Datos: `Afiliado`, `Mascota`, repositories JPA.
- UI: páginas de listado, detalle/mascotas, edición de afiliado y registro inicial.

La identificación del afiliado se mantiene inmutable en la operación de edición. La edición de mascota no recibe afiliado propietario, por lo que no cambia esa relación.