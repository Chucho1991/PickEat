package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.ports.in.RestoreMenuItemUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para restaurar items del menu eliminados lógicamente.
 */
@Service
public class RestoreMenuItemService implements RestoreMenuItemUseCase {
    private final MenuItemRepositoryPort repository;

    public RestoreMenuItemService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Restaura el item indicado.
     *
     * @param id identificador del item.
     * @return item restaurado.
     */
    @Override
    public MenuItem restore(MenuItemId id) {
        MenuItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado"));
        item.setDeleted(false);
        return repository.save(item);
    }
}
