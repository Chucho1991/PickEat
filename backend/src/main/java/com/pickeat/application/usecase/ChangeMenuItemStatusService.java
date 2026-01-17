package com.pickeat.application.usecase;

import com.pickeat.domain.MenuItem;
import com.pickeat.domain.MenuItemId;
import com.pickeat.domain.MenuItemStatus;
import com.pickeat.ports.in.ChangeMenuItemStatusUseCase;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Servicio de aplicación para cambiar el estado del menú.
 */
@Service
public class ChangeMenuItemStatusService implements ChangeMenuItemStatusUseCase {
    private final MenuItemRepositoryPort repository;

    /**
     * Crea el servicio con el repositorio requerido.
     *
     * @param repository repositorio de menú.
     */
    public ChangeMenuItemStatusService(MenuItemRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItem changeStatus(MenuItemId id, MenuItemStatus status) {
        MenuItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ítem de menú no encontrado."));
        item.setStatus(status);
        item.setUpdatedAt(Instant.now());
        return repository.save(item);
    }
}
