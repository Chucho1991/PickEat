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
- **Mesas**: CRUD con activacion, eliminacion logica y estado de ocupacion (libre/ocupada).
- **Menu**: CRUD con activacion/eliminacion logica e indicador de aplica impuesto por producto.
- **Ordenes**: creacion/edicion con calculo de totales, propina opcional (porcentaje o fija), canales de pedido, estados de orden y configuracion por parametros.
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

## Endpoints principales (Ordenes)
- `GET /ordenes`
- `POST /ordenes` (incluye `channelId`, `tipType`, `tipValue`, `tipEnabled`, `billingData`, `couponCode`)
- `PUT /ordenes/{id}` (incluye `billingData`, `couponCode`)
- `GET /ordenes/{id}`
- `GET /ordenes/{id}/cupones`
- `DELETE /ordenes/{id}` (eliminacion logica)
- `POST /ordenes/{id}/restore`
- `POST /ordenes/{id}/active` (solo SUPERADMINISTRADOR)
- `POST /ordenes/{id}/status` (solo ADMIN/SUPERADMIN)
- `GET /ordenes/configuracion`
- `POST /ordenes/configuracion` (solo SUPERADMINISTRADOR)

## Endpoints principales (Canales de orden)
- `GET /ordenes/canales`
- `POST /ordenes/canales` (solo SUPERADMINISTRADOR)
- `PUT /ordenes/canales/{id}` (solo SUPERADMINISTRADOR)
- `DELETE /ordenes/canales/{id}` (solo SUPERADMINISTRADOR, eliminacion logica)
- `POST /ordenes/canales/{id}/restore` (solo SUPERADMINISTRADOR)

## Endpoints principales (Mesas)
- `GET /mesas`
- `POST /mesas`
- `PUT /mesas/{id}`
- `GET /mesas/{id}`
- `DELETE /mesas/{id}` (eliminacion logica)
- `POST /mesas/{id}/restore`
- `POST /mesas/{id}/active` (solo SUPERADMINISTRADOR)

## Soft delete
- Por defecto, los usuarios se eliminan de forma logica.
- La eliminacion fisica es excepcional y solo para SUPERADMINISTRADOR.

## Auditorias
- `api_audit_log`: registra request/response con truncado configurable (`AUDIT_MAX_PAYLOAD_SIZE`).
- `action_audit_log`: registra LOGIN exitoso, creacion, edicion, eliminacion logica y fisica.
