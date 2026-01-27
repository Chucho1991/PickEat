package com.pickeat.ports.in;

import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;

/**
 * Caso de uso para actualizar canales de orden.
 */
public interface UpdateOrderChannelUseCase {
    OrderChannel update(OrderChannelId id, OrderChannel channel);
}
