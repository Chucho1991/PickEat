package com.pickeat.application.usecase;

import com.pickeat.domain.ActionAuditType;
import com.pickeat.domain.User;
import com.pickeat.ports.in.SoftDeleteUserUseCase;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class SoftDeleteUserService implements SoftDeleteUserUseCase {
    private final UserRepositoryPort userRepository;
    private final AuditRepositoryPort auditRepository;

    public SoftDeleteUserService(UserRepositoryPort userRepository, AuditRepositoryPort auditRepository) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    public User softDelete(UUID id, String actor, String actorRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setDeleted(true);
        user.setDeletedAt(Instant.now());
        user.setDeletedBy(actor);
        user.setUpdatedAt(Instant.now());
        User saved = userRepository.save(user);
        auditRepository.saveActionAudit(ActionAuditType.ELIMINADO_LOGICO, "USUARIOS", actor, actorRole,
                saved.getId().toString(), "users");
        return saved;
    }
}
