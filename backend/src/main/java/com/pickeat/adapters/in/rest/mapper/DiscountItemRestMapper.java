package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.DiscountItemRequest;
import com.pickeat.adapters.in.rest.dto.DiscountItemResponse;
import com.pickeat.domain.DiscountApplyScope;
import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountType;

/**
 * Mapper REST para descuentos.
 */
public class DiscountItemRestMapper {
    public DiscountItem toDomain(DiscountItemRequest request) {
        DiscountItem item = DiscountItem.createNew(
                request.getLongDescription(),
                request.getShortDescription(),
                request.getNickname(),
                DiscountType.from(request.getDiscountType()),
                request.getValue(),
                Boolean.TRUE.equals(request.getActivo())
        );
        item.setApplyScope(DiscountApplyScope.from(request.getApplyScope()));
        item.setExclusive(Boolean.TRUE.equals(request.getExclusive()));
        item.setApplyOverDiscount(Boolean.TRUE.equals(request.getApplyOverDiscount()));
        item.setAutoApply(Boolean.TRUE.equals(request.getAutoApply()));
        item.setMenuItemIds(request.getMenuItemIds());
        item.setGeneratesCoupon(Boolean.TRUE.equals(request.getGeneratesCoupon()));
        item.setCouponRuleType(request.getCouponRuleType());
        item.setCouponMinTotal(request.getCouponMinTotal());
        item.setCouponDishType(request.getCouponDishType());
        item.setCouponMinItemQty(request.getCouponMinItemQty());
        item.setCouponValidityDays(request.getCouponValidityDays());
        item.setCouponRequireNoDiscount(request.getCouponRequireNoDiscount() == null || request.getCouponRequireNoDiscount());
        item.setCouponActive(Boolean.TRUE.equals(request.getCouponActive()));
        return item;
    }

    public DiscountItem toUpdateDomain(DiscountItemRequest request) {
        return new DiscountItem(
                null,
                request.getLongDescription(),
                request.getShortDescription(),
                request.getNickname(),
                DiscountType.from(request.getDiscountType()),
                request.getValue(),
                DiscountApplyScope.from(request.getApplyScope()),
                Boolean.TRUE.equals(request.getExclusive()),
                Boolean.TRUE.equals(request.getApplyOverDiscount()),
                Boolean.TRUE.equals(request.getAutoApply()),
                Boolean.TRUE.equals(request.getGeneratesCoupon()),
                request.getCouponRuleType(),
                request.getCouponMinTotal(),
                request.getCouponDishType(),
                request.getCouponMinItemQty(),
                request.getCouponValidityDays(),
                request.getCouponRequireNoDiscount() == null || request.getCouponRequireNoDiscount(),
                Boolean.TRUE.equals(request.getCouponActive()),
                request.getMenuItemIds(),
                Boolean.TRUE.equals(request.getActivo()),
                false,
                null,
                null,
                null
        );
    }

    public DiscountItemResponse toResponse(DiscountItem item) {
        return new DiscountItemResponse(
                item.getId().getValue(),
                item.getLongDescription(),
                item.getShortDescription(),
                item.getNickname(),
                item.getDiscountType().name(),
                item.getValue(),
                item.getApplyScope().name(),
                item.isExclusive(),
                item.isApplyOverDiscount(),
                item.isAutoApply(),
                item.getMenuItemIds(),
                item.isGeneratesCoupon(),
                item.getCouponRuleType(),
                item.getCouponMinTotal(),
                item.getCouponDishType(),
                item.getCouponMinItemQty(),
                item.getCouponValidityDays(),
                item.isCouponRequireNoDiscount(),
                item.isCouponActive(),
                item.isActive(),
                item.isDeleted(),
                item.getImagePath(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
