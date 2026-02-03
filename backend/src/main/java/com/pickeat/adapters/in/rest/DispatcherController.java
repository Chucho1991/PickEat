package com.pickeat.adapters.in.rest;

import com.pickeat.adapters.in.rest.dto.DispatcherItemResponse;
import com.pickeat.domain.DispatcherItem;
import com.pickeat.ports.in.ListDispatcherItemsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

/**
 * Controlador REST para operaciones del despachador.
 */
@RestController
@RequestMapping("/despachador")
public class DispatcherController {
    private final ListDispatcherItemsUseCase listDispatcherItemsUseCase;

    public DispatcherController(ListDispatcherItemsUseCase listDispatcherItemsUseCase) {
        this.listDispatcherItemsUseCase = listDispatcherItemsUseCase;
    }

    /**
     * Entrega la lista de platos visibles en el tablero del despachador.
     *
     * @return lista de platos con su orden y mesa asociada.
     */
    @GetMapping("/comandas")
    @PreAuthorize("hasAnyRole('SUPERADMINISTRADOR','ADMINISTRADOR','DESPACHADOR')")
    public ResponseEntity<List<DispatcherItemResponse>> listItems() {
        List<DispatcherItemResponse> response = listDispatcherItemsUseCase.listAll().stream()
                .sorted(Comparator.comparing(DispatcherItem::getAddedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(item -> new DispatcherItemResponse(
                        item.getOrderId(),
                        item.getMesa(),
                        item.getPlato(),
                        item.getCantidad(),
                        item.getAddedAt()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }
}
