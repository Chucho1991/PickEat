package com.pickeat.application.usecase;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.DiscountType;
import com.pickeat.ports.in.UpdateDiscountItemUseCase;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Servicio de aplicación para actualizar descuentos.
 */
@Service
public class UpdateDiscountItemService implements UpdateDiscountItemUseCase {
    private final DiscountItemRepositoryPort repository;

    public UpdateDiscountItemService(DiscountItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public DiscountItem update(DiscountItemId id, DiscountItem discountItem) {
        DiscountItem existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Descuento no encontrado."));
        if (!existing.getNickname().equalsIgnoreCase(discountItem.getNickname())) {
            repository.findByNickname(discountItem.getNickname()).ifPresent(other -> {
                throw new IllegalArgumentException("El pseudónimo ya está en uso.");
            });
        }
        validateValue(discountItem.getDiscountType(), discountItem.getValue());
        existing.setLongDescription(discountItem.getLongDescription());
        existing.setShortDescription(discountItem.getShortDescription());
        existing.setNickname(discountItem.getNickname());
        existing.setDiscountType(discountItem.getDiscountType());
        existing.setValue(discountItem.getValue());
        existing.setApplyScope(discountItem.getApplyScope());
        existing.setExclusive(discountItem.isExclusive());
        existing.setApplyOverDiscount(discountItem.isApplyOverDiscount());
        existing.setAutoApply(discountItem.isAutoApply());
        existing.setMenuItemIds(discountItem.getMenuItemIds());
        existing.setGeneratesCoupon(discountItem.isGeneratesCoupon());
        existing.setCouponRuleType(discountItem.getCouponRuleType());
        existing.setCouponMinTotal(discountItem.getCouponMinTotal());
        existing.setCouponDishType(discountItem.getCouponDishType());
        existing.setCouponMinItemQty(discountItem.getCouponMinItemQty());
        existing.setCouponValidityDays(discountItem.getCouponValidityDays());
        existing.setCouponRequireNoDiscount(discountItem.isCouponRequireNoDiscount());
        existing.setCouponActive(discountItem.isCouponActive());
        existing.setActive(discountItem.isActive());
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    private void validateValue(DiscountType discountType, BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El valor del descuento debe ser mayor o igual a 0.");
        }
        if (discountType == DiscountType.PERCENTAGE && value.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("El porcentaje no puede superar 100%.");
        }
    }
}
