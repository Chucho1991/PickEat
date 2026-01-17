package com.pickeat.ports.in;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemStatus;

/**
 * Caso de uso para cambiar el estado de un ítem del menú.
 */
public interface ChangeMenuItemStatusUseCase {
    /**
     * Cambia el estado del ítem.
     *
     * @param id identificador del ítem.
     * @param status nuevo estado.
     * @return ítem actualizado.
     */
    MenuItem changeStatus(MenuItemId id, MenuItemStatus status);
}
