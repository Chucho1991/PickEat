package com.pickeat.domain;

import java.time.Instant;

/**
 * Canal de una orden.
 */
public class OrderChannel {
    private OrderChannelId id;
    private String name;
    private boolean active;
    private boolean deleted;
    private boolean isDefault;
    private boolean locked;
    private Instant createdAt;
    private Instant updatedAt;

    public OrderChannel(OrderChannelId id,
                        String name,
                        boolean active,
                        boolean deleted,
                        boolean isDefault,
                        boolean locked,
                        Instant createdAt,
                        Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.deleted = deleted;
        this.isDefault = isDefault;
        this.locked = locked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static OrderChannel createNew(String name) {
        Instant now = Instant.now();
        return new OrderChannel(OrderChannelId.newId(), name, true, false, false, false, now, now);
    }

    public OrderChannelId getId() {
        return id;
    }

    public void setId(OrderChannelId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
