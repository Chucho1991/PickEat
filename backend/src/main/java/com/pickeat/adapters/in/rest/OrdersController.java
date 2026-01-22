package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.OrderConfigResponse;
import com.pickeat.adapters.in.rest.dto.OrderCreateRequest;
import com.pickeat.adapters.in.rest.dto.OrderResponse;
import com.pickeat.adapters.in.rest.mapper.OrderRestMapper;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.ports.in.CreateOrderUseCase;
import com.pickeat.ports.in.GetOrderConfigUseCase;
import com.pickeat.ports.in.GetOrderUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controlador REST para operaciones relacionadas con ordenes.
 */
@RestController
@RequestMapping("/ordenes")
public class OrdersController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final GetOrderConfigUseCase getOrderConfigUseCase;
    private final OrderRestMapper mapper = new OrderRestMapper();

    public OrdersController(CreateOrderUseCase createOrderUseCase,
                            GetOrderUseCase getOrderUseCase,
                            GetOrderConfigUseCase getOrderConfigUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.getOrderConfigUseCase = getOrderConfigUseCase;
    }

    /**
     * Crea una orden con sus items.
     *
     * @param request solicitud con los datos base.
     * @return orden creada.
     */
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request) {
        Order order = createOrderUseCase.create(mapper.toDraft(request));
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    /**
     * Obtiene una orden por id.
     *
     * @param id identificador de la orden.
     * @return orden encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.toResponse(getOrderUseCase.getById(new OrderId(id))));
    }

    /**
     * Obtiene la configuracion de calculos para ordenes.
     *
     * @return configuracion actual.
     */
    @GetMapping("/configuracion")
    public ResponseEntity<OrderConfigResponse> getConfig() {
        return ResponseEntity.ok(mapper.toConfigResponse(getOrderConfigUseCase.getConfig()));
    }
}
