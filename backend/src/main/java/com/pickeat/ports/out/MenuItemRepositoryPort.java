package com.pickeat.ports.out;

import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de items del menu.
 */
public interface MenuItemRepositoryPort {
    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(MenuItemId id);

    Optional<MenuItem> findByNickname(String nickname);

    List<MenuItem> findAll(DishType dishType, Boolean activo, String search, boolean includeDeleted);
}
