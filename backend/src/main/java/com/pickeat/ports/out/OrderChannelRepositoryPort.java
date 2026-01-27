package com.pickeat.ports.out;

import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para canales de orden.
 */
public interface OrderChannelRepositoryPort {
    OrderChannel save(OrderChannel channel);

    Optional<OrderChannel> findById(OrderChannelId id);

    Optional<OrderChannel> findByName(String name);

    Optional<OrderChannel> findDefault();

    List<OrderChannel> findAll(boolean includeDeleted);
}
