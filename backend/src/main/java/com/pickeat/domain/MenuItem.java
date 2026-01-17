package com.pickeat.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidad de dominio para ítems del menú.
 */
public class MenuItem {
    private MenuItemId id;
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
     * Construye un ítem del menú.
     *
     * @param id identificador.
     * @param longDescription descripción larga.
     * @param shortDescription descripción corta.
     * @param nickname pseudónimo.
     * @param dishType tipo de plato.
     * @param status estado.
     * @param price precio.
     * @param imagePath ruta de imagen.
     * @param createdAt fecha de creación.
     * @param updatedAt fecha de actualización.
     */
    public MenuItem(MenuItemId id,
                    String longDescription,
                    String shortDescription,
                    String nickname,
                    DishType dishType,
                    MenuItemStatus status,
                    BigDecimal price,
                    String imagePath,
                    Instant createdAt,
                    Instant updatedAt) {
        this.id = id;
        this.longDescription = longDescription;
        this.shortDescription = shortDescription;
        this.nickname = nickname;
        this.dishType = dishType;
        this.status = status;
        this.price = price;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Crea un nuevo ítem del menú con fechas iniciales.
     *
     * @param longDescription descripción larga.
     * @param shortDescription descripción corta.
     * @param nickname pseudónimo.
     * @param dishType tipo de plato.
     * @param status estado.
     * @param price precio.
     * @return ítem creado.
     */
    public static MenuItem createNew(String longDescription,
                                     String shortDescription,
                                     String nickname,
                                     DishType dishType,
                                     MenuItemStatus status,
                                     BigDecimal price) {
        Instant now = Instant.now();
        return new MenuItem(MenuItemId.newId(), longDescription, shortDescription, nickname,
                dishType, status, price, null, now, now);
    }

    /**
     * Obtiene el identificador.
     *
     * @return id.
     */
    public MenuItemId getId() {
        return id;
    }

    /**
     * Define el identificador.
     *
     * @param id identificador.
     */
    public void setId(MenuItemId id) {
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
     * @param imagePath ruta de la imagen.
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Obtiene la fecha de creación.
     *
     * @return fecha de creación.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Actualiza la fecha de creación.
     *
     * @param createdAt fecha de creación.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene la fecha de actualización.
     *
     * @return fecha de actualización.
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Actualiza la fecha de actualización.
     *
     * @param updatedAt fecha de actualización.
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
