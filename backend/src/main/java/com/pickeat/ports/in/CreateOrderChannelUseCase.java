package com.pickeat.ports.in;

import com.pickeat.domain.OrderChannel;

/**
 * Caso de uso para crear canales de orden.
 */
public interface CreateOrderChannelUseCase {
    OrderChannel create(OrderChannel channel);
}
