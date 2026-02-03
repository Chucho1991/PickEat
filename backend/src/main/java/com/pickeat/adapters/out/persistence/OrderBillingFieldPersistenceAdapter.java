package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.OrderBillingFieldJpaEntity;
import com.pickeat.adapters.out.persistence.repository.OrderBillingFieldJpaRepository;
import com.pickeat.domain.OrderBillingField;
import com.pickeat.ports.out.OrderBillingFieldRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para campos de facturacion.
 */
@Component
public class OrderBillingFieldPersistenceAdapter implements OrderBillingFieldRepositoryPort {
    private final OrderBillingFieldJpaRepository repository;

    public OrderBillingFieldPersistenceAdapter(OrderBillingFieldJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Guarda un campo de facturacion.
     *
     * @param field campo a persistir.
     * @return campo guardado.
     */
    @Override
    public OrderBillingField save(OrderBillingField field) {
        OrderBillingFieldJpaEntity entity = toEntity(field);
        OrderBillingFieldJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    /**
     * Busca un campo por id.
     *
     * @param id identificador del campo.
     * @return campo encontrado.
     */
    @Override
    public Optional<OrderBillingField> findById(java.util.UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    /**
     * Lista los campos configurados.
     *
     * @param includeDeleted indica si se incluyen eliminados.
     * @return listado de campos.
     */
    @Override
    public List<OrderBillingField> findAll(boolean includeDeleted) {
        List<OrderBillingFieldJpaEntity> entities = includeDeleted
                ? repository.findAll()
                : repository.findAllByDeletedFalseOrderBySortOrderAsc();
        return entities.stream().map(this::toDomain).toList();
    }

    private OrderBillingFieldJpaEntity toEntity(OrderBillingField field) {
        OrderBillingFieldJpaEntity entity = new OrderBillingFieldJpaEntity();
        entity.setId(field.getId());
        entity.setLabel(field.getLabel());
        entity.setActive(field.isActive());
        entity.setDeleted(field.isDeleted());
        entity.setSortOrder(field.getSortOrder());
        entity.setCreatedAt(field.getCreatedAt());
        entity.setUpdatedAt(field.getUpdatedAt());
        return entity;
    }

    private OrderBillingField toDomain(OrderBillingFieldJpaEntity entity) {
        return new OrderBillingField(
                entity.getId(),
                entity.getLabel(),
                entity.isActive(),
                entity.isDeleted(),
                entity.getSortOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
