package com.pickeat.adapters.out.persistence.repository;

import com.pickeat.adapters.out.persistence.entity.UserEntity;
import com.pickeat.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByCorreo(String correo);
    Optional<UserEntity> findByUsernameOrCorreo(String username, String correo);
    boolean existsByUsername(String username);
    boolean existsByCorreo(String correo);
    Page<UserEntity> findByRolAndActivoAndDeleted(Role rol, boolean activo, boolean deleted, Pageable pageable);
    Page<UserEntity> findByRolAndActivo(Role rol, boolean activo, Pageable pageable);
    Page<UserEntity> findByRolAndDeleted(Role rol, boolean deleted, Pageable pageable);
    Page<UserEntity> findByActivoAndDeleted(boolean activo, boolean deleted, Pageable pageable);
    Page<UserEntity> findByRol(Role rol, Pageable pageable);
    Page<UserEntity> findByActivo(boolean activo, Pageable pageable);
    Page<UserEntity> findByDeleted(boolean deleted, Pageable pageable);
}
