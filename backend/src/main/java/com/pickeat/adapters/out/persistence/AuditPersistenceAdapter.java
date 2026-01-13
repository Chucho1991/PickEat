package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.ActionAuditLogEntity;
import com.pickeat.adapters.out.persistence.entity.ApiAuditLogEntity;
import com.pickeat.adapters.out.persistence.repository.ActionAuditLogJpaRepository;
import com.pickeat.adapters.out.persistence.repository.ApiAuditLogJpaRepository;
import com.pickeat.domain.ActionAuditType;
import com.pickeat.ports.out.AuditRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class AuditPersistenceAdapter implements AuditRepositoryPort {
    private final ApiAuditLogJpaRepository apiAuditLogRepository;
    private final ActionAuditLogJpaRepository actionAuditLogRepository;

    public AuditPersistenceAdapter(ApiAuditLogJpaRepository apiAuditLogRepository,
                                   ActionAuditLogJpaRepository actionAuditLogRepository) {
        this.apiAuditLogRepository = apiAuditLogRepository;
        this.actionAuditLogRepository = actionAuditLogRepository;
    }

    @Override
    public void saveApiAudit(String endpoint, String requestJson, String responseJson, String usuario, String rolUsuario) {
        ApiAuditLogEntity entity = new ApiAuditLogEntity();
        entity.setFechaHora(Instant.now());
        entity.setEndpoint(endpoint);
        entity.setRequestJson(requestJson);
        entity.setResponseJson(responseJson);
        entity.setUsuario(usuario);
        entity.setRolUsuario(rolUsuario);
        apiAuditLogRepository.save(entity);
    }

    @Override
    public void saveActionAudit(ActionAuditType type, String modulo, String usuario, String rolUsuario, String idRegistro, String nombreTabla) {
        ActionAuditLogEntity entity = new ActionAuditLogEntity();
        entity.setFechaHora(Instant.now());
        entity.setUsuario(usuario);
        entity.setRolUsuario(rolUsuario);
        entity.setModuloAfectado(modulo);
        entity.setTipoModificacion(type.name());
        entity.setIdRegistro(idRegistro);
        entity.setNombreTabla(nombreTabla);
        actionAuditLogRepository.save(entity);
    }
}
