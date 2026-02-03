package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.CouponResponse;
import com.pickeat.adapters.in.rest.dto.OrderActiveRequest;
import com.pickeat.adapters.in.rest.dto.OrderConfigRequest;
import com.pickeat.adapters.in.rest.dto.OrderConfigResponse;
import com.pickeat.adapters.in.rest.dto.OrderCreateRequest;
import com.pickeat.adapters.in.rest.dto.OrderResponse;
import com.pickeat.adapters.in.rest.dto.OrderStatusRequest;
import com.pickeat.adapters.in.rest.mapper.OrderRestMapper;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.domain.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.pickeat.ports.in.ChangeOrderActiveUseCase;
import com.pickeat.ports.in.ChangeOrderStatusUseCase;
import com.pickeat.ports.in.CreateOrderUseCase;
import com.pickeat.ports.in.DeleteOrderUseCase;
import com.pickeat.ports.in.GetOrderConfigUseCase;
import com.pickeat.ports.in.GetOrderUseCase;
import com.pickeat.ports.in.ListOrderCouponsUseCase;
import com.pickeat.ports.in.RestoreOrderUseCase;
import com.pickeat.ports.in.UpdateOrderUseCase;
import com.pickeat.ports.in.UpdateOrderConfigUseCase;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para operaciones relacionadas con ordenes.
 */
@RestController
@RequestMapping("/ordenes")
@Tag(name = "Ordenes", description = "Operaciones de ordenes")
public class OrdersController {
    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final GetOrderConfigUseCase getOrderConfigUseCase;
    private final UpdateOrderConfigUseCase updateOrderConfigUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final RestoreOrderUseCase restoreOrderUseCase;
    private final ChangeOrderActiveUseCase changeOrderActiveUseCase;
    private final ChangeOrderStatusUseCase changeOrderStatusUseCase;
    private final ListOrderCouponsUseCase listOrderCouponsUseCase;
    private final OrderRestMapper mapper = new OrderRestMapper();

    public OrdersController(CreateOrderUseCase createOrderUseCase,
                            UpdateOrderUseCase updateOrderUseCase,
                            GetOrderUseCase getOrderUseCase,
                            GetOrderConfigUseCase getOrderConfigUseCase,
                            UpdateOrderConfigUseCase updateOrderConfigUseCase,
                            DeleteOrderUseCase deleteOrderUseCase,
                            RestoreOrderUseCase restoreOrderUseCase,
                            ChangeOrderActiveUseCase changeOrderActiveUseCase,
                            ChangeOrderStatusUseCase changeOrderStatusUseCase,
                            ListOrderCouponsUseCase listOrderCouponsUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.updateOrderUseCase = updateOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.getOrderConfigUseCase = getOrderConfigUseCase;
        this.updateOrderConfigUseCase = updateOrderConfigUseCase;
        this.deleteOrderUseCase = deleteOrderUseCase;
        this.restoreOrderUseCase = restoreOrderUseCase;
        this.changeOrderActiveUseCase = changeOrderActiveUseCase;
        this.changeOrderStatusUseCase = changeOrderStatusUseCase;
        this.listOrderCouponsUseCase = listOrderCouponsUseCase;
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
     * Actualiza una orden existente.
     *
     * @param id identificador de la orden.
     * @param request datos a actualizar.
     * @return orden actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable("id") UUID id,
                                                @Valid @RequestBody OrderCreateRequest request) {
        Order order = updateOrderUseCase.update(new OrderId(id), mapper.toDraft(request));
        return ResponseEntity.ok(mapper.toResponse(order));
    }

    /**
     * Lista las ordenes registradas.
     *
     * @return listado de ordenes.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> list() {
        List<OrderResponse> orders = getOrderUseCase.getAll().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(orders);
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
     * Lista cupones asociados a una orden.
     *
     * @param id identificador de la orden.
     * @return listado de cupones.
     */
    @GetMapping("/{id}/cupones")
    public ResponseEntity<List<CouponResponse>> listCoupons(@PathVariable("id") UUID id) {
        OrderId orderId = new OrderId(id);
        List<CouponResponse> coupons = listOrderCouponsUseCase.listByOrder(orderId).stream()
                .map(coupon -> {
                    String relation = coupon.getGeneratedOrderId() != null && coupon.getGeneratedOrderId().equals(orderId)
                            ? "GENERATED"
                            : "APPLIED";
                    return new CouponResponse(
                            coupon.getId(),
                            coupon.getCode(),
                            coupon.getDiscountItemId().getValue(),
                            coupon.getStatus().name(),
                            coupon.getExpiresAt(),
                            relation
                    );
                })
                .toList();
        return ResponseEntity.ok(coupons);
    }

    /**
     * Elimina una orden de forma logica.
     *
     * @param id identificador de la orden.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        deleteOrderUseCase.delete(new OrderId(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura una orden eliminada lógicamente.
     *
     * @param id identificador de la orden.
     * @return orden restaurada.
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<OrderResponse> restore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.toResponse(restoreOrderUseCase.restore(new OrderId(id))));
    }

    /**
     * Cambia el estado activo de una orden.
     *
     * @param id identificador de la orden.
     * @param request estado solicitado.
     * @return orden actualizada.
     */
    @PostMapping("/{id}/active")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<OrderResponse> changeActive(@PathVariable("id") UUID id,
                                                      @Valid @RequestBody OrderActiveRequest request) {
        return ResponseEntity.ok(
                mapper.toResponse(changeOrderActiveUseCase.changeActive(new OrderId(id),
                        Boolean.TRUE.equals(request.getActivo())))
        );
    }

    /**
     * Cambia el estado de una orden.
     *
     * @param id identificador de la orden.
     * @param request estado solicitado.
     * @return orden actualizada.
     */
    @PostMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR')")
    @Operation(summary = "Cambia el estado de una orden")
    public ResponseEntity<OrderResponse> changeStatus(@PathVariable("id") UUID id,
                                                      @Valid @RequestBody OrderStatusRequest request) {
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Estado no valido.");
        }
        return ResponseEntity.ok(
                mapper.toResponse(changeOrderStatusUseCase.changeStatus(new OrderId(id), status))
        );
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

    /**
     * Actualiza la configuracion de calculos para ordenes.
     *
     * @param request solicitud de actualizacion.
     * @return configuracion actualizada.
     */
    @PostMapping("/configuracion")
    @PreAuthorize("hasRole('SUPERADMINISTRADOR')")
    public ResponseEntity<OrderConfigResponse> updateConfig(@Valid @RequestBody OrderConfigRequest request) {
        return ResponseEntity.ok(
                mapper.toConfigResponse(updateOrderConfigUseCase.update(request.getTaxRate(), request.getTipValue()))
        );
    }
}
