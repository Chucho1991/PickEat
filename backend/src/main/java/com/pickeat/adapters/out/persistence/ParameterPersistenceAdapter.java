package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.ParameterJpaEntity;
import com.pickeat.adapters.out.persistence.repository.ParameterJpaRepository;
import com.pickeat.domain.AppParameter;
import com.pickeat.ports.out.ParameterRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

/**
 * Adaptador de persistencia para parametros.
 */
@Component
public class ParameterPersistenceAdapter implements ParameterRepositoryPort {
    private final ParameterJpaRepository repository;

    public ParameterPersistenceAdapter(ParameterJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AppParameter> findByKey(String key) {
        return repository.findById(key).map(this::toDomain);
    }

    @Override
    public AppParameter save(AppParameter parameter) {
        ParameterJpaEntity entity = repository.findById(parameter.getKey()).orElseGet(ParameterJpaEntity::new);
        entity.setKey(parameter.getKey());
        entity.setNumericValue(parameter.getNumericValue());
        entity.setTextValue(parameter.getTextValue());
        entity.setBooleanValue(parameter.getBooleanValue());
        entity.setUpdatedAt(Instant.now());
        return toDomain(repository.save(entity));
    }

    private AppParameter toDomain(ParameterJpaEntity entity) {
        return new AppParameter(
                entity.getKey(),
                entity.getNumericValue(),
                entity.getTextValue(),
                entity.getBooleanValue()
        );
    }
}
