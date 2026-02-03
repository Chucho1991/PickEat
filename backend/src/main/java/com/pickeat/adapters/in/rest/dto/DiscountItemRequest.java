package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Solicitud para crear o actualizar un descuento.
 */
public class DiscountItemRequest {
    @NotBlank(message = "La descripcion larga es obligatoria.")
    private String longDescription;

    @NotBlank(message = "La descripcion corta es obligatoria.")
    private String shortDescription;

    @NotBlank(message = "El pseudonimo es obligatorio.")
    private String nickname;

    @NotBlank(message = "El tipo de descuento es obligatorio.")
    private String discountType;

    @NotNull(message = "El estado es obligatorio.")
    private Boolean activo;

    @NotNull(message = "El valor del descuento es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = true, message = "El valor debe ser mayor o igual a 0.")
    private BigDecimal value;

    private String applyScope;

    private Boolean exclusive;

    private Boolean applyOverDiscount;

    private Boolean autoApply;

    private List<UUID> menuItemIds;

    private Boolean generatesCoupon;

    private String couponRuleType;

    private BigDecimal couponMinTotal;

    private String couponDishType;

    private Integer couponMinItemQty;

    private Integer couponValidityDays;

    private Boolean couponRequireNoDiscount;

    private Boolean couponActive;

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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getApplyScope() {
        return applyScope;
    }

    public void setApplyScope(String applyScope) {
        this.applyScope = applyScope;
    }

    public Boolean getExclusive() {
        return exclusive;
    }

    public void setExclusive(Boolean exclusive) {
        this.exclusive = exclusive;
    }

    public Boolean getApplyOverDiscount() {
        return applyOverDiscount;
    }

    public void setApplyOverDiscount(Boolean applyOverDiscount) {
        this.applyOverDiscount = applyOverDiscount;
    }

    public Boolean getAutoApply() {
        return autoApply;
    }

    public void setAutoApply(Boolean autoApply) {
        this.autoApply = autoApply;
    }

    public List<UUID> getMenuItemIds() {
        return menuItemIds;
    }

    public void setMenuItemIds(List<UUID> menuItemIds) {
        this.menuItemIds = menuItemIds;
    }

    public Boolean getGeneratesCoupon() {
        return generatesCoupon;
    }

    public void setGeneratesCoupon(Boolean generatesCoupon) {
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

    public Boolean getCouponRequireNoDiscount() {
        return couponRequireNoDiscount;
    }

    public void setCouponRequireNoDiscount(Boolean couponRequireNoDiscount) {
        this.couponRequireNoDiscount = couponRequireNoDiscount;
    }

    public Boolean getCouponActive() {
        return couponActive;
    }

    public void setCouponActive(Boolean couponActive) {
        this.couponActive = couponActive;
    }
}
