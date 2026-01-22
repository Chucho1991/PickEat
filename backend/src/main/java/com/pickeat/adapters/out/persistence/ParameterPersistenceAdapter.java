package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.ParameterJpaEntity;
import com.pickeat.adapters.out.persistence.repository.ParameterJpaRepository;
import com.pickeat.domain.AppParameter;
import com.pickeat.ports.out.ParameterRepositoryPort;
import org.springframework.stereotype.Component;

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

    private AppParameter toDomain(ParameterJpaEntity entity) {
        return new AppParameter(
                entity.getKey(),
                entity.getNumericValue(),
                entity.getTextValue(),
                entity.getBooleanValue()
        );
    }
}
