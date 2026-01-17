package com.pickeat.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Identificador de un ítem del menú.
 */
public class MenuItemId {
    private final UUID value;

    /**
     * Crea un identificador basado en un UUID.
     *
     * @param value UUID asociado.
     */
    public MenuItemId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("El id del menú es obligatorio.");
        }
        this.value = value;
    }

    /**
     * Genera un nuevo identificador.
     *
     * @return identificador único.
     */
    public static MenuItemId newId() {
        return new MenuItemId(UUID.randomUUID());
    }

    /**
     * Obtiene el UUID asociado.
     *
     * @return valor UUID.
     */
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
        MenuItemId that = (MenuItemId) o;
        return Objects.equals(value, that.value);
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
