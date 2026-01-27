package com.pickeat.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidad de dominio para items del menu.
 */
public class MenuItem {
    private MenuItemId id;
    private String longDescription;
    private String shortDescription;
    private String nickname;
    private DishType dishType;
    private boolean active;
    private boolean deleted;
    private boolean applyTax;
    private BigDecimal price;
    private String imagePath;
    private Instant createdAt;
    private Instant updatedAt;

    public MenuItem(MenuItemId id,
                    String longDescription,
                    String shortDescription,
                    String nickname,
                    DishType dishType,
                    boolean active,
                    boolean deleted,
                    boolean applyTax,
                    BigDecimal price,
                    String imagePath,
                    Instant createdAt,
                    Instant updatedAt) {
        this.id = id;
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
        this.nickname = nickname;
        this.dishType = dishType;
        this.active = active;
        this.deleted = deleted;
        this.applyTax = applyTax;
        this.price = price;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MenuItem createNew(String longDescription,
                                     String shortDescription,
                                     String nickname,
                                     DishType dishType,
                                     boolean active,
                                     boolean applyTax,
                                     BigDecimal price) {
        Instant now = Instant.now();
        return new MenuItem(MenuItemId.newId(), longDescription, shortDescription, nickname,
                dishType, active, false, applyTax, price, null, now, now);
    }

    public MenuItemId getId() {
        return id;
    }

    public void setId(MenuItemId id) {
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

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
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

    public boolean isApplyTax() {
        return applyTax;
    }

    public void setApplyTax(boolean applyTax) {
        this.applyTax = applyTax;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
