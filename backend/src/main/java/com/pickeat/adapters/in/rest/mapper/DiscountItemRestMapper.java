package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.DiscountItemRequest;
import com.pickeat.adapters.in.rest.dto.DiscountItemResponse;
import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountType;

/**
 * Mapper REST para descuentos.
 */
public class DiscountItemRestMapper {
    public DiscountItem toDomain(DiscountItemRequest request) {
        return DiscountItem.createNew(
                request.getLongDescription(),
                request.getShortDescription(),
                request.getNickname(),
                DiscountType.from(request.getDiscountType()),
                request.getValue(),
                Boolean.TRUE.equals(request.getActivo())
        );
    }

    public DiscountItem toUpdateDomain(DiscountItemRequest request) {
        return new DiscountItem(
                null,
                request.getLongDescription(),
                request.getShortDescription(),
                request.getNickname(),
                DiscountType.from(request.getDiscountType()),
                request.getValue(),
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
                item.isActive(),
                item.isDeleted(),
                item.getImagePath(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
