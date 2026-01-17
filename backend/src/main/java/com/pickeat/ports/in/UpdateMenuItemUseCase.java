package com.pickeat.ports.in;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;

/**
 * Caso de uso para actualizar ítems del menú.
 */
public interface UpdateMenuItemUseCase {
    /**
     * Actualiza los datos de un ítem existente.
     *
     * @param id identificador del ítem.
     * @param menuItem datos a actualizar.
     * @return ítem actualizado.
     */
    MenuItem update(MenuItemId id, MenuItem menuItem);
}
