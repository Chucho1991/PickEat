package com.pickeat.application.usecase;

import com.pickeat.domain.ActionAuditType;
import com.pickeat.domain.User;
import com.pickeat.ports.in.CreateUserUseCase;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateUserService implements CreateUserUseCase {
    private final UserRepositoryPort userRepository;
    private final AuditRepositoryPort auditRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserService(UserRepositoryPort userRepository,
                             AuditRepositoryPort auditRepository,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user, String rawPassword, String confirmPassword, String actor, String actorRole) {
        if (!rawPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (userRepository.existsByCorreo(user.getCorreo())) {
            throw new IllegalArgumentException("El correo ya existe.");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El username ya existe.");
        }
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        User saved = userRepository.save(user);
        auditRepository.saveActionAudit(ActionAuditType.CREACION, "USUARIOS", actor, actorRole,
                saved.getId().toString(), "users");
        return saved;
    }
}
