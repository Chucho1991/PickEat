package com.pickeat.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entidad JPA para módulos habilitados por rol.
 */
@Entity
@Table(name = "role_module_access")
public class RoleModuleEntity {
    @EmbeddedId
    private RoleModuleId id;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    protected RoleModuleEntity() {
    }

    public RoleModuleEntity(RoleModuleId id, boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public RoleModuleId getId() {
        return id;
    }

    public void setId(RoleModuleId id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
