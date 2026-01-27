package com.pickeat.adapters.out.persistence.entity;

import com.pickeat.domain.DishType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidad JPA para ítems del menú.
 */
@Entity
@Table(name = "menu_item")
@SQLDelete(sql = "UPDATE menu_item SET deleted = true, active = false WHERE id = ?")
public class MenuItemJpaEntity {
    @Id
    private UUID id;

    @Column(name = "long_description", nullable = false, columnDefinition = "TEXT")
    private String longDescription;

    @Column(name = "short_description", nullable = false, length = 255)
    private String shortDescription;

    @Column(name = "nickname", nullable = false, length = 80, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "dish_type", nullable = false, length = 20)
    private DishType dishType;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "apply_tax", nullable = false)
    private boolean applyTax;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_path", length = 500)
    private String imagePath;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
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
     * Define el identificador.
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
     * Obtiene la ruta de imagen.
     *
     * @return ruta de imagen.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Actualiza la ruta de imagen.
     *
     * @param imagePath ruta de imagen.
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
