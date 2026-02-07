package com.pickeat.adapters.out.mongo;

import com.pickeat.domain.DispatcherItem;
import com.pickeat.ports.out.DispatcherItemsQueryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador de MongoDB para listar los platos del tablero del despachador.
 */
@Component
public class DispatcherMongoAdapter implements DispatcherItemsQueryPort {
    private final KitchenOrderMongoRepository repository;

    public DispatcherMongoAdapter(KitchenOrderMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DispatcherItem> listAll() {
        return repository.findAll().stream()
                .flatMap(order -> getItems(order).stream()
                        .map(item -> toDomain(order, item)))
                .toList();
    }

    private List<KitchenOrderItemDocument> getItems(KitchenOrderDocument document) {
        return document.getItems() != null ? document.getItems() : Collections.emptyList();
    }

    private DispatcherItem toDomain(KitchenOrderDocument order, KitchenOrderItemDocument item) {
        Instant addedAt = item.getAddedAt();
        return new DispatcherItem(
                order.getOrderId(),
                order.getMesa(),
                item.getPlato(),
                item.getCantidad(),
                addedAt
        );
    }
}
