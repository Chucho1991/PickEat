# PickEat

Aplicacion para toma de ordenes en restaurante/cafeteria/delicatessen.

## Servicios
- Frontend (Angular): http://localhost:4200
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## Requisitos
- Docker + Docker Compose

## Variables de entorno
Revisar `.env.example` para valores por defecto:
- `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_URL`
- `MONGO_URI`
- `JWT_SECRET`, `JWT_EXPIRATION_MINUTES`
- `AUDIT_MAX_PAYLOAD_SIZE`
- `CORS_ALLOWED_ORIGINS`

## Levantar con Docker
```bash
cp .env.example .env
docker compose up --build
```

## Modulos
- **Usuarios**: completo (auth, CRUD, perfil, soft delete, restore, delete fisico con rol SUPERADMINISTRADOR).
- **Mesas/Menu**: CRUD base con activacion y eliminacion logica.
- **Ordenes**: creacion con calculo de totales en vivo y configuracion por parametros.
- **Despachador**: rutas placeholder con "Coming soon".

## Gestion de usuarios
- Solo roles **ADMINISTRADOR** y **SUPERADMINISTRADOR** pueden crear/listar usuarios.
- En creacion de usuarios se requiere contrasena y confirmacion (minimo 8 caracteres).
- Los roles disponibles: `SUPERADMINISTRADOR`, `ADMINISTRADOR`, `MESERO`, `DESPACHADOR`.

## Endpoints principales (Usuarios)
- `POST /auth/login`
- `GET /users` (paginacion y filtros: `rol`, `activo`, `deleted`)
- `POST /users`
- `GET /users/{id}`
- `PUT /users/{id}`
- `POST /users/{id}/soft-delete`
- `POST /users/{id}/restore`
- `DELETE /users/{id}` (solo SUPERADMINISTRADOR, eliminacion fisica)
- `GET /users/me`
- `PUT /users/me`
- `GET /dashboard` (solo ADMIN/SUPERADMIN)

## Soft delete
- Por defecto, los usuarios se eliminan de forma logica.
- La eliminacion fisica es excepcional y solo para SUPERADMINISTRADOR.

## Auditorias
- `api_audit_log`: registra request/response con truncado configurable (`AUDIT_MAX_PAYLOAD_SIZE`).
- `action_audit_log`: registra LOGIN exitoso, creacion, edicion, eliminacion logica y fisica.
