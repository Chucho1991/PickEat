package com.pickeat.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Identificador de orden.
 */
public class OrderId {
    private final UUID value;

    public OrderId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("El id de la orden es obligatorio.");
        }
        this.value = value;
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderId)) {
            return false;
        }
        OrderId orderId = (OrderId) o;
        return Objects.equals(value, orderId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
