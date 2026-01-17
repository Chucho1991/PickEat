package com.pickeat.adapters.in.rest.dto;

import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItemStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Solicitud para crear o actualizar un ítem del menú.
 */
public class MenuItemRequest {
    @NotBlank(message = "La descripción larga es obligatoria.")
    private String longDescription;

    @NotBlank(message = "La descripción corta es obligatoria.")
    private String shortDescription;

    @NotBlank(message = "El pseudónimo es obligatorio.")
    private String nickname;

    @NotNull(message = "El tipo de plato es obligatorio.")
    private DishType dishType;

    @NotNull(message = "El estado es obligatorio.")
    private MenuItemStatus status;

    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio debe ser mayor o igual a 0.")
    private BigDecimal price;

    /**
     * Obtiene la descripción larga.
     *
     * @return descripción larga.
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Actualiza la descripción larga.
     *
     * @param longDescription descripción larga.
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * Obtiene la descripción corta.
     *
     * @return descripción corta.
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Actualiza la descripción corta.
     *
     * @param shortDescription descripción corta.
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
}
