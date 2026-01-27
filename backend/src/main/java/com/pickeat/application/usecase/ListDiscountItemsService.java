package com.pickeat.application.usecase;

import com.pickeat.domain.DiscountItem;
import com.pickeat.ports.in.ListDiscountItemsUseCase;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de aplicación para listar descuentos.
 */
@Service
public class ListDiscountItemsService implements ListDiscountItemsUseCase {
    private final DiscountItemRepositoryPort repository;

    public ListDiscountItemsService(DiscountItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<DiscountItem> list(Boolean activo, String search, boolean includeDeleted) {
        return repository.findAll(activo, search, includeDeleted);
    }
}
