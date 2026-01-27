package com.pickeat.ports.in;

import com.pickeat.domain.OrderConfig;

import java.math.BigDecimal;

/**
 * Caso de uso para actualizar configuracion de ordenes.
 */
public interface UpdateOrderConfigUseCase {
    /**
     * Actualiza los parametros de impuestos y propina.
     *
     * @param taxRate porcentaje de impuesto.
     * @param tipValue porcentaje de propina.
     * @return configuracion actualizada.
     */
    OrderConfig update(BigDecimal taxRate, BigDecimal tipValue);
}
