package com.pickeat.application.usecase;

import com.pickeat.domain.ActionAuditType;
import com.pickeat.domain.User;
import com.pickeat.ports.in.UpdateUserUseCase;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateUserService implements UpdateUserUseCase {
    private final UserRepositoryPort userRepository;
    private final AuditRepositoryPort auditRepository;

    public UpdateUserService(UserRepositoryPort userRepository, AuditRepositoryPort auditRepository) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    public User update(UUID id, User update, String actor, String actorRole) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        userRepository.findByCorreo(update.getCorreo())
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new IllegalArgumentException("El correo ya existe.");
                });
        userRepository.findByUsername(update.getUsername())
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new IllegalArgumentException("El username ya existe.");
                });
        existing.setNombres(update.getNombres());
        existing.setCorreo(update.getCorreo());
        existing.setUsername(update.getUsername());
        existing.setRol(update.getRol());
        existing.setActivo(update.isActivo());
        existing.setUpdatedAt(Instant.now());
        User saved = userRepository.save(existing);
        auditRepository.saveActionAudit(ActionAuditType.EDICION, "USUARIOS", actor, actorRole,
                saved.getId().toString(), "users");
        return saved;
    }
}
