package com.pickeat.ports.in;

import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;

import java.util.List;

/**
 * Caso de uso para listar items del menu.
 */
public interface ListMenuItemsUseCase {
    List<MenuItem> list(DishType dishType, Boolean activo, String search, boolean includeDeleted);
}
