package com.pickeat.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    private DiscountApplyScope applyScope;
    private boolean exclusive;
    private boolean applyOverDiscount;
    private boolean autoApply;
    private boolean generatesCoupon;
    private String couponRuleType;
    private BigDecimal couponMinTotal;
    private String couponDishType;
    private Integer couponMinItemQty;
    private Integer couponValidityDays;
    private boolean couponRequireNoDiscount;
    private boolean couponActive;
    private List<UUID> menuItemIds;
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
                        DiscountApplyScope applyScope,
                        boolean exclusive,
                        boolean applyOverDiscount,
                        boolean autoApply,
                        boolean generatesCoupon,
                        String couponRuleType,
                        BigDecimal couponMinTotal,
                        String couponDishType,
                        Integer couponMinItemQty,
                        Integer couponValidityDays,
                        boolean couponRequireNoDiscount,
                        boolean couponActive,
                        List<UUID> menuItemIds,
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
        this.applyScope = applyScope == null ? DiscountApplyScope.ORDER : applyScope;
        this.exclusive = exclusive;
        this.applyOverDiscount = applyOverDiscount;
        this.autoApply = autoApply;
        this.generatesCoupon = generatesCoupon;
        this.couponRuleType = couponRuleType;
        this.couponMinTotal = couponMinTotal;
        this.couponDishType = couponDishType;
        this.couponMinItemQty = couponMinItemQty;
        this.couponValidityDays = couponValidityDays;
        this.couponRequireNoDiscount = couponRequireNoDiscount;
        this.couponActive = couponActive;
        this.menuItemIds = menuItemIds == null ? Collections.emptyList() : menuItemIds;
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
                discountType, value, DiscountApplyScope.ORDER, false, false, false, false,
                null, null, null, null, 7, true, false, List.of(), active, false, null, now, now);
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

    public DiscountApplyScope getApplyScope() {
        return applyScope;
    }

    public void setApplyScope(DiscountApplyScope applyScope) {
        this.applyScope = applyScope == null ? DiscountApplyScope.ORDER : applyScope;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isApplyOverDiscount() {
        return applyOverDiscount;
    }

    public void setApplyOverDiscount(boolean applyOverDiscount) {
        this.applyOverDiscount = applyOverDiscount;
    }

    public boolean isAutoApply() {
        return autoApply;
    }

    public void setAutoApply(boolean autoApply) {
        this.autoApply = autoApply;
    }

    public boolean isGeneratesCoupon() {
        return generatesCoupon;
    }

    public void setGeneratesCoupon(boolean generatesCoupon) {
        this.generatesCoupon = generatesCoupon;
    }

    public String getCouponRuleType() {
        return couponRuleType;
    }

    public void setCouponRuleType(String couponRuleType) {
        this.couponRuleType = couponRuleType;
    }

    public BigDecimal getCouponMinTotal() {
        return couponMinTotal;
    }

    public void setCouponMinTotal(BigDecimal couponMinTotal) {
        this.couponMinTotal = couponMinTotal;
    }

    public String getCouponDishType() {
        return couponDishType;
    }

    public void setCouponDishType(String couponDishType) {
        this.couponDishType = couponDishType;
    }

    public Integer getCouponMinItemQty() {
        return couponMinItemQty;
    }

    public void setCouponMinItemQty(Integer couponMinItemQty) {
        this.couponMinItemQty = couponMinItemQty;
    }

    public Integer getCouponValidityDays() {
        return couponValidityDays;
    }

    public void setCouponValidityDays(Integer couponValidityDays) {
        this.couponValidityDays = couponValidityDays;
    }

    public boolean isCouponRequireNoDiscount() {
        return couponRequireNoDiscount;
    }

    public void setCouponRequireNoDiscount(boolean couponRequireNoDiscount) {
        this.couponRequireNoDiscount = couponRequireNoDiscount;
    }

    public boolean isCouponActive() {
        return couponActive;
    }

    public void setCouponActive(boolean couponActive) {
        this.couponActive = couponActive;
    }

    public List<UUID> getMenuItemIds() {
        return menuItemIds;
    }

    public void setMenuItemIds(List<UUID> menuItemIds) {
        this.menuItemIds = menuItemIds == null ? Collections.emptyList() : menuItemIds;
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
