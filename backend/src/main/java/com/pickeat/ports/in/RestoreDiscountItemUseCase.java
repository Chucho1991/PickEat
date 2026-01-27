package com.pickeat.ports.in;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;

/**
 * Caso de uso para restaurar descuentos.
 */
public interface RestoreDiscountItemUseCase {
    DiscountItem restore(DiscountItemId id);
}
