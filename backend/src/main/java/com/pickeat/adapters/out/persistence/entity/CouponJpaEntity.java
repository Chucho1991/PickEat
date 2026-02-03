package com.pickeat.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidad JPA para cupones.
 */
@Entity
@Table(name = "discount_coupon")
public class CouponJpaEntity {
    @Id
    private UUID id;

    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;

    @Column(name = "discount_item_id", nullable = false)
    private UUID discountItemId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "generated_order_id")
    private UUID generatedOrderId;

    @Column(name = "redeemed_order_id")
    private UUID redeemedOrderId;

    @Column(name = "redeemed_at")
    private Instant redeemedAt;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UUID getDiscountItemId() {
        return discountItemId;
    }

    public void setDiscountItemId(UUID discountItemId) {
        this.discountItemId = discountItemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UUID getGeneratedOrderId() {
        return generatedOrderId;
    }

    public void setGeneratedOrderId(UUID generatedOrderId) {
        this.generatedOrderId = generatedOrderId;
    }

    public UUID getRedeemedOrderId() {
        return redeemedOrderId;
    }

    public void setRedeemedOrderId(UUID redeemedOrderId) {
        this.redeemedOrderId = redeemedOrderId;
    }

    public Instant getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(Instant redeemedAt) {
        this.redeemedAt = redeemedAt;
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
