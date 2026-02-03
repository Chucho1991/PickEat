package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.OrderBillingFieldRequest;
import com.pickeat.adapters.in.rest.dto.OrderBillingFieldResponse;
import com.pickeat.domain.OrderBillingField;
import com.pickeat.ports.in.CreateOrderBillingFieldUseCase;
import com.pickeat.ports.in.DeleteOrderBillingFieldUseCase;
import com.pickeat.ports.in.ListOrderBillingFieldsUseCase;
import com.pickeat.ports.in.RestoreOrderBillingFieldUseCase;
import com.pickeat.ports.in.UpdateOrderBillingFieldUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para campos de facturacion.
 */
@RestController
@RequestMapping("/ordenes/configuracion/facturacion")
@Tag(name = "Ordenes - Facturacion", description = "Configuracion de campos de facturacion")
public class OrderBillingFieldsController {
    private final ListOrderBillingFieldsUseCase listUseCase;
    private final CreateOrderBillingFieldUseCase createUseCase;
    private final UpdateOrderBillingFieldUseCase updateUseCase;
    private final DeleteOrderBillingFieldUseCase deleteUseCase;
    private final RestoreOrderBillingFieldUseCase restoreUseCase;

    public OrderBillingFieldsController(ListOrderBillingFieldsUseCase listUseCase,
                                        CreateOrderBillingFieldUseCase createUseCase,
                                        UpdateOrderBillingFieldUseCase updateUseCase,
                                        DeleteOrderBillingFieldUseCase deleteUseCase,
                                        RestoreOrderBillingFieldUseCase restoreUseCase) {
        this.listUseCase = listUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
        this.restoreUseCase = restoreUseCase;
    }

    /**
     * Lista los campos configurados para facturacion.
     *
     * @param includeDeleted indica si se incluyen eliminados.
     * @return listado de campos.
     */
    @GetMapping
    public ResponseEntity<List<OrderBillingFieldResponse>> list(@RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted) {
        List<OrderBillingFieldResponse> fields = listUseCase.list(includeDeleted).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(fields);
    }

    /**
     * Crea un campo de facturacion.
     *
     * @param request datos del campo.
     * @return campo creado.
     */
    @PostMapping
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<OrderBillingFieldResponse> create(@Valid @RequestBody OrderBillingFieldRequest request) {
        OrderBillingField created = createUseCase.create(
                OrderBillingField.createNew(request.getLabel(), request.getSortOrder() == null ? 0 : request.getSortOrder())
        );
        return ResponseEntity.ok(toResponse(created));
    }

    /**
     * Actualiza un campo de facturacion.
     *
     * @param id identificador del campo.
     * @param request datos actualizados.
     * @return campo actualizado.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<OrderBillingFieldResponse> update(@PathVariable("id") UUID id,
                                                            @Valid @RequestBody OrderBillingFieldRequest request) {
        OrderBillingField updated = updateUseCase.update(id, request.getLabel(), request.getSortOrder(), request.getActive());
        return ResponseEntity.ok(toResponse(updated));
    }

    /**
     * Elimina un campo de facturacion de forma logica.
     *
     * @param id identificador del campo.
     * @return respuesta vacia.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        deleteUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un campo eliminado.
     *
     * @param id identificador del campo.
     * @return campo restaurado.
     */
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<OrderBillingFieldResponse> restore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(toResponse(restoreUseCase.restore(id)));
    }

    private OrderBillingFieldResponse toResponse(OrderBillingField field) {
        return new OrderBillingFieldResponse(
                field.getId(),
                field.getLabel(),
                field.isActive(),
                field.isDeleted(),
                field.getSortOrder(),
                field.getCreatedAt(),
                field.getUpdatedAt()
        );
    }
}
