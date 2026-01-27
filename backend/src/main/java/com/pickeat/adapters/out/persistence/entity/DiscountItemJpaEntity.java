package com.pickeat.adapters.out.persistence.entity;

import com.pickeat.domain.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidad JPA para descuentos.
 */
@Entity
@Table(name = "discount_item")
@SQLDelete(sql = "UPDATE discount_item SET deleted = true, active = false WHERE id = ?")
public class DiscountItemJpaEntity {
    @Id
    private UUID id;

    @Column(name = "long_description", nullable = false, columnDefinition = "TEXT")
    private String longDescription;

    @Column(name = "short_description", nullable = false, length = 255)
    private String shortDescription;

    @Column(name = "nickname", nullable = false, length = 80, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    @Column(name = "value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "image_path", length = 500)
    private String imagePath;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
