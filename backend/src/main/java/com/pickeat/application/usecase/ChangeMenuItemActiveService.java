package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.ports.in.ChangeMenuItemActiveUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para activar o inactivar items del menu.
 */
@Service
public class ChangeMenuItemActiveService implements ChangeMenuItemActiveUseCase {
    private final MenuItemRepositoryPort repository;

    public ChangeMenuItemActiveService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public MenuItem changeActive(MenuItemId id, boolean active) {
        MenuItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item de menu no encontrado."));
        item.setActive(active);
        return repository.save(item);
    }
}
