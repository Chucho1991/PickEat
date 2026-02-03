package com.pickeat.application.usecase;

import com.pickeat.domain.OrderBillingField;
import com.pickeat.ports.in.CreateOrderBillingFieldUseCase;
import com.pickeat.ports.in.DeleteOrderBillingFieldUseCase;
import com.pickeat.ports.in.ListOrderBillingFieldsUseCase;
import com.pickeat.ports.in.RestoreOrderBillingFieldUseCase;
import com.pickeat.ports.in.UpdateOrderBillingFieldUseCase;
import com.pickeat.ports.out.OrderBillingFieldRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de aplicacion para campos de facturacion.
 */
@Service
public class OrderBillingFieldService implements
        ListOrderBillingFieldsUseCase,
        CreateOrderBillingFieldUseCase,
        UpdateOrderBillingFieldUseCase,
        DeleteOrderBillingFieldUseCase,
        RestoreOrderBillingFieldUseCase {
    private final OrderBillingFieldRepositoryPort repository;

    public OrderBillingFieldService(OrderBillingFieldRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Lista los campos configurados.
     *
     * @param includeDeleted indica si se incluyen eliminados.
     * @return listado de campos.
     */
    @Override
    public List<OrderBillingField> list(boolean includeDeleted) {
        return repository.findAll(includeDeleted);
    }

    /**
     * Crea un nuevo campo de facturacion.
     *
     * @param field datos base.
     * @return campo creado.
     */
    @Override
    public OrderBillingField create(OrderBillingField field) {
        if (field == null || field.getLabel() == null || field.getLabel().isBlank()) {
            throw new IllegalArgumentException("El nombre del campo es obligatorio.");
        }
        int sortOrder = Math.max(0, field.getSortOrder());
        OrderBillingField created = OrderBillingField.createNew(field.getLabel().trim(), sortOrder);
        return repository.save(created);
    }

    /**
     * Actualiza un campo existente.
     *
     * @param id identificador del campo.
     * @param label etiqueta del campo.
     * @param sortOrder orden de visualizacion.
     * @param active estado activo.
     * @return campo actualizado.
     */
    @Override
    public OrderBillingField update(UUID id, String label, Integer sortOrder, Boolean active) {
        OrderBillingField existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El campo no existe."));
        if (label != null && !label.isBlank()) {
            existing.setLabel(label.trim());
        }
        if (sortOrder != null) {
            existing.setSortOrder(Math.max(0, sortOrder));
        }
        if (active != null) {
            existing.setActive(active);
        }
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    /**
     * Elimina un campo de forma logica.
     *
     * @param id identificador del campo.
     */
    @Override
    public void delete(UUID id) {
        OrderBillingField existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El campo no existe."));
        existing.setDeleted(true);
        existing.setActive(false);
        existing.setUpdatedAt(Instant.now());
        repository.save(existing);
    }

    /**
     * Restaura un campo eliminado.
     *
     * @param id identificador del campo.
     * @return campo restaurado.
     */
    @Override
    public OrderBillingField restore(UUID id) {
        OrderBillingField existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El campo no existe."));
        existing.setDeleted(false);
        existing.setActive(true);
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }
}
