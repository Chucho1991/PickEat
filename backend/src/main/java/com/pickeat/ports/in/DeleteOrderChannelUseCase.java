package com.pickeat.ports.in;

import com.pickeat.domain.OrderChannelId;

/**
 * Caso de uso para eliminar canales de orden.
 */
public interface DeleteOrderChannelUseCase {
    void delete(OrderChannelId id);
}
