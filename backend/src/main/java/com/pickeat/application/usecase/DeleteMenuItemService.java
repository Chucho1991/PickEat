package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItemId;
import com.pickeat.ports.in.DeleteMenuItemUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Caso de uso para eliminar items del menu de forma logica.
 */
@Service
public class DeleteMenuItemService implements DeleteMenuItemUseCase {
    private final MenuItemRepositoryPort repository;

    public DeleteMenuItemService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void delete(MenuItemId id) {
        repository.findById(id).ifPresent(item -> {
            item.setDeleted(true);
            item.setActive(false);
            repository.save(item);
        });
    }
}
