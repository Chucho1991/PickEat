package com.pickeat.adapters.in.rest.dto;

import java.time.Instant;

/**
 * Respuesta para un plato en el tablero del despachador.
 */
public record DispatcherItemResponse(String orderId, String mesa, String plato, int cantidad, Instant addedAt) {
}
