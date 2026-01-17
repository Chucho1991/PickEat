package com.pickeat.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Identificador de mesa.
 */
public class MesaId {
    private final UUID value;

    public MesaId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("El id de mesa es obligatorio.");
        }
        this.value = value;
    }

    public static MesaId newId() {
        return new MesaId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MesaId)) {
            return false;
        }
        MesaId mesaId = (MesaId) o;
        return Objects.equals(value, mesaId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
