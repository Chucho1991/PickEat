package com.pickeat.application.usecase;

import com.pickeat.domain.ActionAuditType;
import com.pickeat.domain.User;
import com.pickeat.ports.in.UpdateMeUseCase;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateMeService implements UpdateMeUseCase {
    private final UserRepositoryPort userRepository;
    private final AuditRepositoryPort auditRepository;
    private final PasswordEncoder passwordEncoder;

    public UpdateMeService(UserRepositoryPort userRepository,
                           AuditRepositoryPort auditRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User updateMe(String username, User update, String rawPassword, String confirmPassword) {
        User existing = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        userRepository.findByCorreo(update.getCorreo())
                .filter(user -> !user.getId().equals(existing.getId()))
                .ifPresent(user -> {
                    throw new IllegalArgumentException("El correo ya existe.");
                });
        userRepository.findByUsername(update.getUsername())
                .filter(user -> !user.getId().equals(existing.getId()))
                .ifPresent(user -> {
                    throw new IllegalArgumentException("El username ya existe.");
                });
        existing.setNombres(update.getNombres());
        existing.setCorreo(update.getCorreo());
        existing.setUsername(update.getUsername());
        if (rawPassword != null && !rawPassword.isBlank()) {
            if (!rawPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            existing.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        existing.setUpdatedAt(Instant.now());
        User saved = userRepository.save(existing);
        auditRepository.saveActionAudit(ActionAuditType.EDICION, "USUARIOS", saved.getUsername(),
                saved.getRol().name(), saved.getId().toString(), "users");
        return saved;
    }
}
