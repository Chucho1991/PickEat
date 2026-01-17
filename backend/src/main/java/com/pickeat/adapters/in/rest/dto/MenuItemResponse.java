package com.pickeat.adapters.in.rest.dto;

import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItemStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Respuesta con datos del ítem de menú.
 */
public class MenuItemResponse {
    private UUID id;
    private String longDescription;
    private String shortDescription;
    private String nickname;
    private DishType dishType;
    private MenuItemStatus status;
    private BigDecimal price;
    private String imagePath;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Obtiene el identificador.
     *
     * @return id.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Actualiza el identificador.
     *
     * @param id identificador.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Obtiene la descripción larga.
     *
     * @return descripción.
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Actualiza la descripción larga.
     *
     * @param longDescription descripción.
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * Obtiene la descripción corta.
     *
     * @return descripción.
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Actualiza la descripción corta.
     *
     * @param shortDescription descripción.
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Obtiene el pseudónimo.
     *
     * @return pseudónimo.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Actualiza el pseudónimo.
     *
     * @param nickname pseudónimo.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Obtiene el tipo de plato.
     *
     * @return tipo de plato.
     */
    public DishType getDishType() {
        return dishType;
    }

    /**
     * Actualiza el tipo de plato.
     *
     * @param dishType tipo de plato.
     */
    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }

    /**
     * Obtiene el estado.
     *
     * @return estado.
     */
    public MenuItemStatus getStatus() {
        return status;
    }

    /**
     * Actualiza el estado.
     *
     * @param status estado.
     */
    public void setStatus(MenuItemStatus status) {
        this.status = status;
    }

    /**
     * Obtiene el precio.
     *
     * @return precio.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Actualiza el precio.
     *
     * @param price precio.
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Obtiene la ruta de la imagen.
     *
     * @return ruta de imagen.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Actualiza la ruta de la imagen.
     *
     * @param imagePath ruta de imagen.
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Obtiene fecha de creación.
     *
     * @return fecha.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Actualiza fecha de creación.
     *
     * @param createdAt fecha.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene fecha de actualización.
     *
     * @return fecha.
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Actualiza fecha de actualización.
     *
     * @param updatedAt fecha.
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
