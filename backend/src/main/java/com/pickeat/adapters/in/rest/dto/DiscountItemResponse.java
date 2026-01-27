package com.pickeat.adapters.in.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Respuesta REST de un descuento.
 */
public class DiscountItemResponse {
    private UUID id;
    private String longDescription;
    private String shortDescription;
    private String nickname;
    private String discountType;
    private BigDecimal value;
    private boolean activo;
    private boolean deleted;
    private String imagePath;
    private Instant createdAt;
    private Instant updatedAt;

    public DiscountItemResponse(UUID id,
                                String longDescription,
                                String shortDescription,
                                String nickname,
                                String discountType,
                                BigDecimal value,
                                boolean activo,
                                boolean deleted,
                                String imagePath,
                                Instant createdAt,
                                Instant updatedAt) {
        this.id = id;
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
        this.nickname = nickname;
        this.discountType = discountType;
        this.value = value;
        this.activo = activo;
        this.deleted = deleted;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDiscountType() {
        return discountType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
