package com.pickeat.application.usecase;

import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountType;
import com.pickeat.ports.in.CreateDiscountItemUseCase;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Servicio de aplicación para crear descuentos.
 */
@Service
public class CreateDiscountItemService implements CreateDiscountItemUseCase {
    private final DiscountItemRepositoryPort repository;

    public CreateDiscountItemService(DiscountItemRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public DiscountItem create(DiscountItem discountItem) {
        validateValue(discountItem.getDiscountType(), discountItem.getValue());
        repository.findByNickname(discountItem.getNickname()).ifPresent(existing -> {
            throw new IllegalArgumentException("El pseudónimo ya está en uso.");
        });
        return repository.save(discountItem);
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
