package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.ports.in.GetMenuItemUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación para consultar ítems del menú.
 */
@Service
public class GetMenuItemService implements GetMenuItemUseCase {
    private final MenuItemRepositoryPort repository;

    /**
     * Construye el servicio con el repositorio requerido.
     *
     * @param repository repositorio de menú.
     */
    public GetMenuItemService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItem getById(MenuItemId id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ítem de menú no encontrado."));
    }
}
