package com.pickeat.ports.out;

import com.pickeat.domain.Role;
import com.pickeat.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    Optional<User> findByCorreo(String correo);
    Optional<User> findByUsernameOrCorreo(String username, String correo);
    Page<User> findAll(Role role, Boolean activo, Boolean deleted, Pageable pageable);
    void deleteById(UUID id);
    boolean existsByUsername(String username);
    boolean existsByCorreo(String correo);
}
