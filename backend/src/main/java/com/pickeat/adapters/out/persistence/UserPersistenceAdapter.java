package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.UserEntity;
import com.pickeat.adapters.out.persistence.repository.UserJpaRepository;
import com.pickeat.domain.Role;
import com.pickeat.domain.User;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserPersistenceAdapter implements UserRepositoryPort {
    private final UserJpaRepository userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = userJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public Optional<User> findByCorreo(String correo) {
        return userJpaRepository.findByCorreo(correo).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsernameOrCorreo(String username, String correo) {
        return userJpaRepository.findByUsernameOrCorreo(username, correo).map(this::toDomain);
    }

    @Override
    public Page<User> findAll(Role role, Boolean activo, Boolean deleted, Pageable pageable) {
        if (role != null && activo != null && deleted != null) {
            return userJpaRepository.findByRolAndActivoAndDeleted(role, activo, deleted, pageable).map(this::toDomain);
        }
        if (role != null && activo != null) {
            return userJpaRepository.findByRolAndActivo(role, activo, pageable).map(this::toDomain);
        }
        if (role != null && deleted != null) {
            return userJpaRepository.findByRolAndDeleted(role, deleted, pageable).map(this::toDomain);
        }
        if (activo != null && deleted != null) {
            return userJpaRepository.findByActivoAndDeleted(activo, deleted, pageable).map(this::toDomain);
        }
        if (role != null) {
            return userJpaRepository.findByRol(role, pageable).map(this::toDomain);
        }
        if (activo != null) {
            return userJpaRepository.findByActivo(activo, pageable).map(this::toDomain);
        }
        if (deleted != null) {
            return userJpaRepository.findByDeleted(deleted, pageable).map(this::toDomain);
        }
        return userJpaRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return userJpaRepository.existsByCorreo(correo);
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setNombres(user.getNombres());
        entity.setCorreo(user.getCorreo());
        entity.setUsername(user.getUsername());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setRol(user.getRol());
        entity.setActivo(user.isActivo());
        entity.setDeleted(user.isDeleted());
        entity.setDeletedAt(user.getDeletedAt());
        entity.setDeletedBy(user.getDeletedBy());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getNombres(),
                entity.getCorreo(),
                entity.getUsername(),
                entity.getPasswordHash(),
                entity.getRol(),
                entity.isActivo(),
                entity.isDeleted(),
                entity.getDeletedAt(),
                entity.getDeletedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
