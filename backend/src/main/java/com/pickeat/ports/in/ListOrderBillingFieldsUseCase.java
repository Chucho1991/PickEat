package com.pickeat.ports.in;

import com.pickeat.domain.OrderBillingField;

import java.util.List;

/**
 * Caso de uso para listar campos de facturacion.
 */
public interface ListOrderBillingFieldsUseCase {
    /**
     * Lista campos configurados.
     *
     * @param includeDeleted indica si se incluyen eliminados.
     * @return listado de campos.
     */
    List<OrderBillingField> list(boolean includeDeleted);
}
