package com.pickeat.application.usecase;

import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemStatus;
import com.pickeat.ports.in.ListMenuItemsUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de aplicación para listar ítems del menú.
 */
@Service
public class ListMenuItemsService implements ListMenuItemsUseCase {
    private final MenuItemRepositoryPort repository;

    /**
     * Construye el servicio con el repositorio requerido.
     *
     * @param repository repositorio de menú.
     */
    public ListMenuItemsService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItem> list(DishType dishType, MenuItemStatus status, String search) {
        String normalizedSearch = search == null || search.isBlank() ? null : search.trim();
        return repository.findAll(dishType, status, normalizedSearch);
    }
}
