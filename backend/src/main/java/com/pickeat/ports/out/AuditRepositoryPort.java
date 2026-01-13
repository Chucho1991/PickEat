package com.pickeat.ports.out;

import com.pickeat.domain.ActionAuditType;

public interface AuditRepositoryPort {
    void saveApiAudit(String endpoint, String requestJson, String responseJson, String usuario, String rolUsuario);
    void saveActionAudit(ActionAuditType type, String modulo, String usuario, String rolUsuario, String idRegistro, String nombreTabla);
}
