package com.pickeat.adapters.in.rest.dto;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID id;
    private String nombres;
    private String username;
    private String rol;

    public AuthResponse(String token, UUID id, String nombres, String username, String rol) {
        this.token = token;
        this.id = id;
        this.nombres = nombres;
        this.username = username;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public UUID getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }
}
