package com.pickeat.application.usecase;

import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.ports.in.ListMenuItemsUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de aplicacion para listar items del menu.
 */
@Service
public class ListMenuItemsService implements ListMenuItemsUseCase {
    private final MenuItemRepositoryPort repository;

    public ListMenuItemsService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<MenuItem> list(DishType dishType, Boolean activo, String search, boolean includeDeleted) {
        String normalizedSearch = search == null || search.isBlank() ? null : search.trim();
        return repository.findAll(dishType, activo, normalizedSearch, includeDeleted);
    }
}
