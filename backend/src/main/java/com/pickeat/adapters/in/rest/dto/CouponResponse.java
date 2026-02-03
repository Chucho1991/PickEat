package com.pickeat.adapters.in.rest.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * Respuesta REST de un cupon.
 */
public class CouponResponse {
    private UUID id;
    private String code;
    private UUID discountItemId;
    private String status;
    private Instant expiresAt;
    private String relation;

    public CouponResponse(UUID id,
                          String code,
                          UUID discountItemId,
                          String status,
                          Instant expiresAt,
                          String relation) {
        this.id = id;
        this.code = code;
        this.discountItemId = discountItemId;
        this.status = status;
        this.expiresAt = expiresAt;
        this.relation = relation;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public UUID getDiscountItemId() {
        return discountItemId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public String getRelation() {
        return relation;
    }
}
