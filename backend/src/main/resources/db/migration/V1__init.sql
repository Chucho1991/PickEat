CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    nombres VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    activo BOOLEAN NOT NULL,
    deleted BOOLEAN NOT NULL,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS api_audit_log (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    request_json TEXT,
    response_json TEXT,
    usuario VARCHAR(255),
    rol_usuario VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS action_audit_log (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP NOT NULL,
    usuario VARCHAR(255),
    rol_usuario VARCHAR(255),
    modulo_afectado VARCHAR(255),
    tipo_modificacion VARCHAR(50),
    id_registro VARCHAR(255),
    nombre_tabla VARCHAR(255)
);
