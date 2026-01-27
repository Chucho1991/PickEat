package com.pickeat.ports.in;

import com.pickeat.domain.DiscountItem;

import java.util.List;

/**
 * Caso de uso para listar descuentos.
 */
public interface ListDiscountItemsUseCase {
    List<DiscountItem> list(Boolean activo, String search, boolean includeDeleted);
}
