package com.pickeat.ports.out;

import com.pickeat.domain.OrderBillingField;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para campos de facturacion.
 */
public interface OrderBillingFieldRepositoryPort {
    /**
     * Guarda un campo de facturacion.
     *
     * @param field campo a persistir.
     * @return campo guardado.
     */
    OrderBillingField save(OrderBillingField field);

    /**
     * Busca un campo por id.
     *
     * @param id identificador del campo.
     * @return campo encontrado.
     */
    Optional<OrderBillingField> findById(UUID id);

    /**
     * Lista los campos configurados.
     *
     * @param includeDeleted indica si se incluyen eliminados.
     * @return listado de campos.
     */
    List<OrderBillingField> findAll(boolean includeDeleted);
}
