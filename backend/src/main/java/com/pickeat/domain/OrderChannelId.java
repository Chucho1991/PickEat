package com.pickeat.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Identificador de canal de orden.
 */
public class OrderChannelId {
    private final UUID value;

    public OrderChannelId(UUID value) {
        this.value = value;
    }

    public static OrderChannelId newId() {
        return new OrderChannelId(UUID.randomUUID());
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
        OrderChannelId that = (OrderChannelId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
