package com.pickeat.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidad de dominio para descuentos.
 */
public class DiscountItem {
    private DiscountItemId id;
    private String longDescription;
    private String shortDescription;
    private String nickname;
    private DiscountType discountType;
    private BigDecimal value;
    private boolean active;
    private boolean deleted;
    private String imagePath;
    private Instant createdAt;
    private Instant updatedAt;

    public DiscountItem(DiscountItemId id,
                        String longDescription,
                        String shortDescription,
                        String nickname,
                        DiscountType discountType,
                        BigDecimal value,
                        boolean active,
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
        this.active = active;
        this.deleted = deleted;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DiscountItem createNew(String longDescription,
                                         String shortDescription,
                                         String nickname,
                                         DiscountType discountType,
                                         BigDecimal value,
                                         boolean active) {
        Instant now = Instant.now();
        return new DiscountItem(DiscountItemId.newId(), longDescription, shortDescription, nickname,
                discountType, value, active, false, null, now, now);
    }

    public DiscountItemId getId() {
        return id;
    }

    public void setId(DiscountItemId id) {
        this.id = id;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
