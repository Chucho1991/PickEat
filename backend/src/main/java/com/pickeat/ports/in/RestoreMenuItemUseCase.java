package com.pickeat.ports.in;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;

/**
 * Caso de uso para restaurar items del menu eliminados lógicamente.
 */
public interface RestoreMenuItemUseCase {
    /**
     * Restaura el item del menu indicado.
     *
     * @param id identificador del item.
     * @return item restaurado.
     */
    MenuItem restore(MenuItemId id);
}
