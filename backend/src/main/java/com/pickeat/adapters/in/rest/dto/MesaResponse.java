package com.pickeat.adapters.in.rest.dto;

import java.util.UUID;

/**
 * Respuesta para representar una mesa.
 */
public record MesaResponse(UUID id, String description, int seats, boolean activo, boolean deleted, boolean ocupada) {
}
