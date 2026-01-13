package com.pickeat.domain;

public enum Role {
    SUPERADMINISTRADOR,
    ADMINISTRADOR,
    MESERO,
    DESPACHADOR;

    public static Role from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }
        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Rol no reconocido: " + value);
        }
    }
}
