package com.pickeat.ports.in;

import com.pickeat.domain.MenuItem;

/**
 * Caso de uso para crear ítems de menú.
 */
public interface CreateMenuItemUseCase {
    /**
     * Registra un nuevo ítem del menú.
     *
     * @param menuItem entidad de dominio.
     * @return ítem creado.
     */
    MenuItem create(MenuItem menuItem);
}
