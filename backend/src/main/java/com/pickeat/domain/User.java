package com.pickeat.domain;

import java.time.Instant;
import java.util.UUID;

public class User {
    private UUID id;
    private String nombres;
    private String correo;
    private String username;
    private String passwordHash;
    private Role rol;
    private boolean activo;
    private boolean deleted;
    private Instant deletedAt;
    private String deletedBy;
    private Instant createdAt;
    private Instant updatedAt;

    public User(UUID id, String nombres, String correo, String username, String passwordHash, Role rol,
                boolean activo, boolean deleted, Instant deletedAt, String deletedBy,
                Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.nombres = nombres;
        this.correo = correo;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = activo;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User createNew(String nombres, String correo, String username, String passwordHash, Role rol) {
        Instant now = Instant.now();
        return new User(UUID.randomUUID(), nombres, correo, username, passwordHash, rol, true, false, null, null, now, now);
    }

    public UUID getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
