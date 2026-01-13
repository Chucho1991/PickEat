package com.pickeat.application.usecase;

import com.pickeat.domain.ActionAuditType;
import com.pickeat.domain.User;
import com.pickeat.ports.in.RestoreUserUseCase;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RestoreUserService implements RestoreUserUseCase {
    private final UserRepositoryPort userRepository;
    private final AuditRepositoryPort auditRepository;

    public RestoreUserService(UserRepositoryPort userRepository, AuditRepositoryPort auditRepository) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    public User restore(UUID id, String actor, String actorRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setDeleted(false);
        user.setDeletedAt(null);
        user.setDeletedBy(null);
        user.setUpdatedAt(Instant.now());
        User saved = userRepository.save(user);
        auditRepository.saveActionAudit(ActionAuditType.EDICION, "USUARIOS", actor, actorRole,
                saved.getId().toString(), "users");
        return saved;
    }
}
