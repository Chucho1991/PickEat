package com.pickeat.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidad de dominio para cupones.
 */
public class Coupon {
    private UUID id;
    private String code;
    private DiscountItemId discountItemId;
    private CouponStatus status;
    private Instant expiresAt;
    private OrderId generatedOrderId;
    private OrderId redeemedOrderId;
    private Instant redeemedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public Coupon(UUID id,
                  String code,
                  DiscountItemId discountItemId,
                  CouponStatus status,
                  Instant expiresAt,
                  OrderId generatedOrderId,
                  OrderId redeemedOrderId,
                  Instant redeemedAt,
                  Instant createdAt,
                  Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.discountItemId = discountItemId;
        this.status = status;
        this.expiresAt = expiresAt;
        this.generatedOrderId = generatedOrderId;
        this.redeemedOrderId = redeemedOrderId;
        this.redeemedAt = redeemedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Coupon createNew(String code, DiscountItemId discountItemId, Instant expiresAt, OrderId generatedOrderId) {
        Instant now = Instant.now();
        return new Coupon(UUID.randomUUID(), code, discountItemId, CouponStatus.ACTIVE, expiresAt,
                generatedOrderId, null, null, now, now);
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountItemId getDiscountItemId() {
        return discountItemId;
    }

    public void setDiscountItemId(DiscountItemId discountItemId) {
        this.discountItemId = discountItemId;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public OrderId getGeneratedOrderId() {
        return generatedOrderId;
    }

    public void setGeneratedOrderId(OrderId generatedOrderId) {
        this.generatedOrderId = generatedOrderId;
    }

    public OrderId getRedeemedOrderId() {
        return redeemedOrderId;
    }

    public void setRedeemedOrderId(OrderId redeemedOrderId) {
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
