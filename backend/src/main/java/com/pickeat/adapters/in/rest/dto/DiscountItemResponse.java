package com.pickeat.adapters.in.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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
    private String applyScope;
    private boolean exclusive;
    private boolean applyOverDiscount;
    private boolean autoApply;
    private List<UUID> menuItemIds;
    private boolean generatesCoupon;
    private String couponRuleType;
    private BigDecimal couponMinTotal;
    private String couponDishType;
    private Integer couponMinItemQty;
    private Integer couponValidityDays;
    private boolean couponRequireNoDiscount;
    private boolean couponActive;
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
                                String applyScope,
                                boolean exclusive,
                                boolean applyOverDiscount,
                                boolean autoApply,
                                List<UUID> menuItemIds,
                                boolean generatesCoupon,
                                String couponRuleType,
                                BigDecimal couponMinTotal,
                                String couponDishType,
                                Integer couponMinItemQty,
                                Integer couponValidityDays,
                                boolean couponRequireNoDiscount,
                                boolean couponActive,
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
        this.applyScope = applyScope;
        this.exclusive = exclusive;
        this.applyOverDiscount = applyOverDiscount;
        this.autoApply = autoApply;
        this.menuItemIds = menuItemIds;
        this.generatesCoupon = generatesCoupon;
        this.couponRuleType = couponRuleType;
        this.couponMinTotal = couponMinTotal;
        this.couponDishType = couponDishType;
        this.couponMinItemQty = couponMinItemQty;
        this.couponValidityDays = couponValidityDays;
        this.couponRequireNoDiscount = couponRequireNoDiscount;
        this.couponActive = couponActive;
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

    public String getApplyScope() {
        return applyScope;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public boolean isApplyOverDiscount() {
        return applyOverDiscount;
    }

    public boolean isAutoApply() {
        return autoApply;
    }

    public List<UUID> getMenuItemIds() {
        return menuItemIds;
    }

    public boolean isGeneratesCoupon() {
        return generatesCoupon;
    }

    public String getCouponRuleType() {
        return couponRuleType;
    }

    public BigDecimal getCouponMinTotal() {
        return couponMinTotal;
    }

    public String getCouponDishType() {
        return couponDishType;
    }

    public Integer getCouponMinItemQty() {
        return couponMinItemQty;
    }

    public Integer getCouponValidityDays() {
        return couponValidityDays;
    }

    public boolean isCouponRequireNoDiscount() {
        return couponRequireNoDiscount;
    }

    public boolean isCouponActive() {
        return couponActive;
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
