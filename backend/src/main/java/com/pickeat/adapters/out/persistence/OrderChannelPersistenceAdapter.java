package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.OrderChannelJpaEntity;
import com.pickeat.adapters.out.persistence.repository.OrderChannelJpaRepository;
import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para canales de orden.
 */
@Component
public class OrderChannelPersistenceAdapter implements OrderChannelRepositoryPort {
    private final OrderChannelJpaRepository repository;

    public OrderChannelPersistenceAdapter(OrderChannelJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrderChannel save(OrderChannel channel) {
        OrderChannelJpaEntity entity = repository.findById(channel.getId().getValue()).orElseGet(OrderChannelJpaEntity::new);
        entity.setId(channel.getId().getValue());
        entity.setName(channel.getName());
        entity.setActive(channel.isActive());
        entity.setDeleted(channel.isDeleted());
        entity.setDefault(channel.isDefault());
        entity.setLocked(channel.isLocked());
        entity.setCreatedAt(channel.getCreatedAt() == null ? Instant.now() : channel.getCreatedAt());
        entity.setUpdatedAt(Instant.now());
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<OrderChannel> findById(OrderChannelId id) {
        return repository.findById(id.getValue()).map(this::toDomain);
    }

    @Override
    public Optional<OrderChannel> findByName(String name) {
        return repository.findByName(name).map(this::toDomain);
    }

    @Override
    public Optional<OrderChannel> findDefault() {
        return repository.findDefault().map(this::toDomain);
    }

    @Override
    public List<OrderChannel> findAll(boolean includeDeleted) {
        List<OrderChannelJpaEntity> entities = includeDeleted ? repository.findAll() : repository.findAllByDeletedFalse();
        return entities.stream().map(this::toDomain).toList();
    }

    private OrderChannel toDomain(OrderChannelJpaEntity entity) {
        return new OrderChannel(
                new OrderChannelId(entity.getId()),
                entity.getName(),
                entity.isActive(),
                entity.isDeleted(),
                entity.isDefault(),
                entity.isLocked(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
