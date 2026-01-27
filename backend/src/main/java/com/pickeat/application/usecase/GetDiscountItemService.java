package com.pickeat.application.usecase;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.ports.in.GetDiscountItemUseCase;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación para consultar descuentos.
 */
@Service
public class GetDiscountItemService implements GetDiscountItemUseCase {
    private final DiscountItemRepositoryPort repository;

    public GetDiscountItemService(DiscountItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public DiscountItem getById(DiscountItemId id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Descuento no encontrado."));
    }
}
