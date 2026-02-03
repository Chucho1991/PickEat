package com.pickeat.adapters.out.persistence.entity;

import com.pickeat.domain.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import com.pickeat.domain.DiscountApplyScope;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "apply_scope", nullable = false, length = 20)
    private DiscountApplyScope applyScope;

    @Column(name = "exclusive", nullable = false)
    private boolean exclusive;

    @Column(name = "apply_over_discount", nullable = false)
    private boolean applyOverDiscount;

    @Column(name = "auto_apply", nullable = false)
    private boolean autoApply;

    @Column(name = "generates_coupon", nullable = false)
    private boolean generatesCoupon;

    @Column(name = "coupon_rule_type", length = 30)
    private String couponRuleType;

    @Column(name = "coupon_min_total", precision = 10, scale = 2)
    private BigDecimal couponMinTotal;

    @Column(name = "coupon_dish_type", length = 30)
    private String couponDishType;

    @Column(name = "coupon_min_item_qty")
    private Integer couponMinItemQty;

    @Column(name = "coupon_validity_days")
    private Integer couponValidityDays;

    @Column(name = "coupon_require_no_discount", nullable = false)
    private boolean couponRequireNoDiscount;

    @Column(name = "coupon_active", nullable = false)
    private boolean couponActive;

    @ElementCollection
    @CollectionTable(name = "discount_item_menu_item", joinColumns = @JoinColumn(name = "discount_item_id"))
    @Column(name = "menu_item_id")
    private List<UUID> menuItemIds;

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

    public DiscountApplyScope getApplyScope() {
        return applyScope;
    }

    public void setApplyScope(DiscountApplyScope applyScope) {
        this.applyScope = applyScope;
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
        this.menuItemIds = menuItemIds;
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
