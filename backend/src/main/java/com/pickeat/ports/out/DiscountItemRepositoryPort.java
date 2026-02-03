package com.pickeat.ports.out;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto para persistencia de descuentos.
 */
public interface DiscountItemRepositoryPort {
    DiscountItem save(DiscountItem discountItem);

    Optional<DiscountItem> findById(DiscountItemId id);

    Optional<DiscountItem> findByNickname(String nickname);

    List<DiscountItem> findAll(Boolean activo, String search, boolean includeDeleted);

    /**
     * Lista descuentos configurados para generar cupones.
     *
     * @return descuentos generadores.
     */
    List<DiscountItem> findCouponGenerators();

    /**
     * Lista descuentos que aplican automaticamente a items.
     *
     * @return descuentos auto-aplicables.
     */
    List<DiscountItem> findAutoApplyItemDiscounts();
}
