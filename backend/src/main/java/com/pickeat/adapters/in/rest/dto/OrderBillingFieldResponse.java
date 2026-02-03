package com.pickeat.adapters.in.rest.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Respuesta para campos de facturacion.
 */
public class OrderBillingFieldResponse {
    private UUID id;
    private String label;
    private boolean active;
    private boolean deleted;
    private int sortOrder;
    private Instant createdAt;
    private Instant updatedAt;

    public OrderBillingFieldResponse(UUID id,
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

    public UUID getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
