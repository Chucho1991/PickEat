package com.pickeat.adapters.out.persistence.entity;

import com.pickeat.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;
import java.util.Objects;

/**
 * Identificador compuesto para módulos habilitados por rol.
 */
@Embeddable
public class RoleModuleId implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 50)
    private Role rol;

    @Column(name = "module_key", nullable = false, length = 100)
    private String moduleKey;

    protected RoleModuleId() {
    }

    public RoleModuleId(Role rol, String moduleKey) {
        this.rol = rol;
        this.moduleKey = moduleKey;
    }

    public Role getRol() {
        return rol;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleModuleId that = (RoleModuleId) o;
        return rol == that.rol && Objects.equals(moduleKey, that.moduleKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rol, moduleKey);
    }
}
