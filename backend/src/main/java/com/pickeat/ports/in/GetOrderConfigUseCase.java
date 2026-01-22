package com.pickeat.ports.in;

import com.pickeat.domain.OrderConfig;

/**
 * Caso de uso para obtener configuracion de ordenes.
 */
public interface GetOrderConfigUseCase {
    OrderConfig getConfig();
}
