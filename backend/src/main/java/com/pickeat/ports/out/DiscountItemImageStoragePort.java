package com.pickeat.ports.out;

import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.DiscountItemImage;

/**
 * Puerto para almacenar imagenes de descuentos.
 */
public interface DiscountItemImageStoragePort {
    String store(DiscountItemId id, DiscountItemImage image);
}
