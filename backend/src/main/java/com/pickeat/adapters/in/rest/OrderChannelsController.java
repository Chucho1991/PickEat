package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.OrderChannelRequest;
import com.pickeat.adapters.in.rest.dto.OrderChannelResponse;
import com.pickeat.adapters.in.rest.mapper.OrderChannelRestMapper;
import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;
import com.pickeat.ports.in.CreateOrderChannelUseCase;
import com.pickeat.ports.in.DeleteOrderChannelUseCase;
import com.pickeat.ports.in.ListOrderChannelsUseCase;
import com.pickeat.ports.in.RestoreOrderChannelUseCase;
import com.pickeat.ports.in.UpdateOrderChannelUseCase;
import io.swagger.v3.oas.annotations.Operation;
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
 * Controlador REST para canales de orden.
 */
@RestController
@RequestMapping("/ordenes/canales")
@Tag(name = "Ordenes - Canales", description = "Administracion de canales de pedido")
public class OrderChannelsController {
    private final ListOrderChannelsUseCase listOrderChannelsUseCase;
    private final CreateOrderChannelUseCase createOrderChannelUseCase;
    private final UpdateOrderChannelUseCase updateOrderChannelUseCase;
    private final DeleteOrderChannelUseCase deleteOrderChannelUseCase;
    private final RestoreOrderChannelUseCase restoreOrderChannelUseCase;
    private final OrderChannelRestMapper mapper = new OrderChannelRestMapper();

    public OrderChannelsController(ListOrderChannelsUseCase listOrderChannelsUseCase,
                                   CreateOrderChannelUseCase createOrderChannelUseCase,
                                   UpdateOrderChannelUseCase updateOrderChannelUseCase,
                                   DeleteOrderChannelUseCase deleteOrderChannelUseCase,
                                   RestoreOrderChannelUseCase restoreOrderChannelUseCase) {
        this.listOrderChannelsUseCase = listOrderChannelsUseCase;
        this.createOrderChannelUseCase = createOrderChannelUseCase;
        this.updateOrderChannelUseCase = updateOrderChannelUseCase;
        this.deleteOrderChannelUseCase = deleteOrderChannelUseCase;
        this.restoreOrderChannelUseCase = restoreOrderChannelUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista los canales de pedido")
    public ResponseEntity<List<OrderChannelResponse>> list(@RequestParam(value = "includeDeleted", required = false) Boolean includeDeleted) {
        List<OrderChannelResponse> channels = listOrderChannelsUseCase.list(Boolean.TRUE.equals(includeDeleted))
                .stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(channels);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    @Operation(summary = "Crea un canal de pedido")
    public ResponseEntity<OrderChannelResponse> create(@Valid @RequestBody OrderChannelRequest request) {
        OrderChannel channel = createOrderChannelUseCase.create(mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(channel));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    @Operation(summary = "Actualiza un canal de pedido")
    public ResponseEntity<OrderChannelResponse> update(@PathVariable("id") UUID id,
                                                       @Valid @RequestBody OrderChannelRequest request) {
        OrderChannel channel = updateOrderChannelUseCase.update(new OrderChannelId(id), mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(channel));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    @Operation(summary = "Elimina un canal de pedido de forma logica")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        deleteOrderChannelUseCase.delete(new OrderChannelId(id));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    @Operation(summary = "Restaura un canal de pedido eliminado")
    public ResponseEntity<OrderChannelResponse> restore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.toResponse(restoreOrderChannelUseCase.restore(new OrderChannelId(id))));
    }
}
