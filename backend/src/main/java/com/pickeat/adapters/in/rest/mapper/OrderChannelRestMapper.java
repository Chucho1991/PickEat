package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.OrderChannelRequest;
import com.pickeat.adapters.in.rest.dto.OrderChannelResponse;
import com.pickeat.domain.OrderChannel;

/**
 * Mapper REST para canales de orden.
 */
public class OrderChannelRestMapper {
    public OrderChannel toDomain(OrderChannelRequest request) {
        return OrderChannel.createNew(request.getName());
    }

    public OrderChannelResponse toResponse(OrderChannel channel) {
        OrderChannelResponse response = new OrderChannelResponse();
        response.setId(channel.getId().getValue());
        response.setName(channel.getName());
        response.setActivo(channel.isActive());
        response.setDeleted(channel.isDeleted());
        response.setLocked(channel.isLocked());
        response.setDefault(channel.isDefault());
        response.setCreatedAt(channel.getCreatedAt());
        response.setUpdatedAt(channel.getUpdatedAt());
        return response;
    }
}
