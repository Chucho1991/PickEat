package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.MesaJpaEntity;
import com.pickeat.adapters.out.persistence.repository.MesaJpaRepository;
import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;
import com.pickeat.ports.out.MesaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para mesas.
 */
@Component
public class MesaPersistenceAdapter implements MesaRepositoryPort {
    private final MesaJpaRepository repository;

    public MesaPersistenceAdapter(MesaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mesa save(Mesa mesa) {
        MesaJpaEntity entity = toEntity(mesa);
        MesaJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Mesa> findById(MesaId id) {
        return repository.findById(id.getValue()).map(this::toDomain);
    }

    @Override
    public List<Mesa> findAll(boolean includeDeleted) {
        return repository.findAllFiltered(includeDeleted).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(MesaId id) {
        repository.deleteById(id.getValue());
    }

    private MesaJpaEntity toEntity(Mesa mesa) {
        MesaJpaEntity entity = new MesaJpaEntity();
        entity.setId(mesa.getId().getValue());
        entity.setDescription(mesa.getDescription());
        entity.setSeats(mesa.getSeats());
        entity.setActive(mesa.isActive());
        entity.setDeleted(mesa.isDeleted());
        entity.setOccupied(mesa.isOccupied());
        return entity;
    }

    private Mesa toDomain(MesaJpaEntity entity) {
        return new Mesa(
                new MesaId(entity.getId()),
                entity.getDescription(),
                entity.getSeats(),
                entity.isActive(),
                entity.isDeleted(),
                entity.isOccupied()
        );
    }
}
