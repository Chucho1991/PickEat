package com.pickeat.domain;

/**
 * Tipo de descuento disponible.
 */
public enum DiscountType {
    FIXED,
    PERCENTAGE;

    public static DiscountType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El tipo de descuento es obligatorio.");
        }
        try {
            return DiscountType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de descuento no reconocido: " + value);
        }
    }
}
