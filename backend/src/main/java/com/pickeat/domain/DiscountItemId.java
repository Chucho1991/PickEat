package com.pickeat.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Identificador de descuentos.
 */
public class DiscountItemId {
    private final UUID value;

    public DiscountItemId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("El id del descuento es obligatorio.");
        }
        this.value = value;
    }

    public static DiscountItemId newId() {
        return new DiscountItemId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscountItemId that = (DiscountItemId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
