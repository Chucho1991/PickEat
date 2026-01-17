package com.pickeat.ports.in;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;

/**
 * Caso de uso para consultar un ítem de menú.
 */
public interface GetMenuItemUseCase {
    /**
     * Obtiene un ítem por su identificador.
     *
     * @param id identificador.
     * @return ítem encontrado.
     */
    MenuItem getById(MenuItemId id);
}
