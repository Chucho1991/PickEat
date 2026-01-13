package com.pickeat.adapters.in.rest.dto;

import java.time.Instant;
import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String nombres;
    private String correo;
    private String username;
    private String rol;
    private boolean activo;
    private boolean deleted;
    private Instant deletedAt;
    private String deletedBy;
    private Instant createdAt;
    private Instant updatedAt;

    public UserResponse(UUID id, String nombres, String correo, String username, String rol,
                        boolean activo, boolean deleted, Instant deletedAt, String deletedBy,
                        Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.nombres = nombres;
        this.correo = correo;
        this.username = username;
        this.rol = rol;
        this.activo = activo;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
