package com.pickeat.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Campo configurable para datos de facturacion.
 */
public class OrderBillingField {
    private UUID id;
    private String label;
    private boolean active;
    private boolean deleted;
    private int sortOrder;
    private Instant createdAt;
    private Instant updatedAt;

    public OrderBillingField(UUID id,
                             String label,
                             boolean active,
                             boolean deleted,
                             int sortOrder,
                             Instant createdAt,
                             Instant updatedAt) {
        this.id = id;
        this.label = label;
        this.active = active;
        this.deleted = deleted;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static OrderBillingField createNew(String label, int sortOrder) {
        Instant now = Instant.now();
        return new OrderBillingField(UUID.randomUUID(), label, true, false, sortOrder, now, now);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
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
