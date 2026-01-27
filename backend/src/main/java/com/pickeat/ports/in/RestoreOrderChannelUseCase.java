package com.pickeat.ports.in;

import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;

/**
 * Caso de uso para restaurar canales de orden.
 */
public interface RestoreOrderChannelUseCase {
    OrderChannel restore(OrderChannelId id);
}
