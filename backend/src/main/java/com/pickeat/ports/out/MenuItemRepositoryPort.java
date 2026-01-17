package com.pickeat.ports.out;

import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de ítems del menú.
 */
public interface MenuItemRepositoryPort {
    /**
     * Persiste el ítem del menú.
     *
     * @param menuItem ítem a guardar.
     * @return ítem persistido.
     */
    MenuItem save(MenuItem menuItem);

    /**
     * Busca un ítem por id.
     *
     * @param id identificador.
     * @return ítem opcional.
     */
    Optional<MenuItem> findById(MenuItemId id);

    /**
     * Busca un ítem por pseudónimo.
     *
     * @param nickname pseudónimo.
     * @return ítem opcional.
     */
    Optional<MenuItem> findByNickname(String nickname);

    /**
     * Lista ítems aplicando filtros.
     *
     * @param dishType tipo de plato.
     * @param status estado.
     * @param search término de búsqueda.
     * @return listado filtrado.
     */
    List<MenuItem> findAll(DishType dishType, MenuItemStatus status, String search);
}
