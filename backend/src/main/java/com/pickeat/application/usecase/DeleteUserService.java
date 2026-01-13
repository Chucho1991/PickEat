package com.pickeat.application.usecase;

import com.pickeat.domain.ActionAuditType;
import com.pickeat.ports.in.DeleteUserUseCase;
import com.pickeat.ports.out.AuditRepositoryPort;
import com.pickeat.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteUserService implements DeleteUserUseCase {
    private final UserRepositoryPort userRepository;
    private final AuditRepositoryPort auditRepository;

    public DeleteUserService(UserRepositoryPort userRepository, AuditRepositoryPort auditRepository) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    public void delete(UUID id, String actor, String actorRole) {
        userRepository.deleteById(id);
        auditRepository.saveActionAudit(ActionAuditType.ELIMINADO_FISICO, "USUARIOS", actor, actorRole,
                id.toString(), "users");
    }
}
