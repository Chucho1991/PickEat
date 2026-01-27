package com.pickeat.ports.in;

import com.pickeat.domain.OrderChannel;

import java.util.List;

/**
 * Caso de uso para listar canales de orden.
 */
public interface ListOrderChannelsUseCase {
    List<OrderChannel> list(boolean includeDeleted);
}
