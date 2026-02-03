package com.pickeat.domain;

/**
 * Define el alcance del descuento.
 */
public enum DiscountApplyScope {
    ORDER,
    ITEM;

    public static DiscountApplyScope from(String value) {
        if (value == null || value.isBlank()) {
            return ORDER;
        }
        try {
            return DiscountApplyScope.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ORDER;
        }
    }
}
