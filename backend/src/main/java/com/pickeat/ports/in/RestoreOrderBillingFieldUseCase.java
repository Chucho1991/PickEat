package com.pickeat.ports.in;

import com.pickeat.domain.OrderBillingField;

import java.util.UUID;

/**
 * Caso de uso para restaurar campos de facturacion.
 */
public interface RestoreOrderBillingFieldUseCase {
    /**
     * Restaura un campo eliminado.
     *
     * @param id identificador del campo.
     * @return campo restaurado.
     */
    OrderBillingField restore(UUID id);
}
