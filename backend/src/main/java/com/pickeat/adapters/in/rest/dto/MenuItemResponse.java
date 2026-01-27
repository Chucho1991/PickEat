package com.pickeat.adapters.in.rest.dto;

import com.pickeat.domain.DishType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Respuesta con datos del item de menu.
 */
public class MenuItemResponse {
    private UUID id;
    private String longDescription;
    private String shortDescription;
    private String nickname;
    private DishType dishType;
    private boolean activo;
    private boolean deleted;
    private boolean aplicaImpuesto;
    private BigDecimal price;
    private String imagePath;
    private Instant createdAt;
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

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isAplicaImpuesto() {
        return aplicaImpuesto;
    }

    public void setAplicaImpuesto(boolean aplicaImpuesto) {
        this.aplicaImpuesto = aplicaImpuesto;
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
