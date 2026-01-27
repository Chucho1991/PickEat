package com.pickeat.ports.in;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;

/**
 * Caso de uso para consultar descuentos.
 */
public interface GetDiscountItemUseCase {
    DiscountItem getById(DiscountItemId id);
}
