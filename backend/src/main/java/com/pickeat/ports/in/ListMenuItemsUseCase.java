package com.pickeat.ports.in;

import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemStatus;

import java.util.List;

/**
 * Caso de uso para listar ítems del menú.
 */
public interface ListMenuItemsUseCase {
    /**
     * Obtiene los ítems filtrados.
     *
     * @param dishType tipo de plato.
     * @param status estado.
     * @param search término de búsqueda.
     * @return listado de ítems.
     */
    List<MenuItem> list(DishType dishType, MenuItemStatus status, String search);
}
