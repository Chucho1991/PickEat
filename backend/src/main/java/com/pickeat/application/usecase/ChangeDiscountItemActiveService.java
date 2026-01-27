package com.pickeat.application.usecase;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.ports.in.ChangeDiscountItemActiveUseCase;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación para activar/desactivar descuentos.
 */
@Service
public class ChangeDiscountItemActiveService implements ChangeDiscountItemActiveUseCase {
    private final DiscountItemRepositoryPort repository;

    public ChangeDiscountItemActiveService(DiscountItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public DiscountItem changeActive(DiscountItemId id, boolean active) {
        DiscountItem item = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Descuento no encontrado."));
        if (item.isDeleted()) {
            throw new IllegalArgumentException("El descuento esta eliminado.");
        }
        item.setActive(active);
        return repository.save(item);
    }
}
