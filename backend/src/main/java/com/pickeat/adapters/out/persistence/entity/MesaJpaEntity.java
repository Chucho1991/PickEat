package com.pickeat.adapters.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;

import java.util.UUID;

/**
 * Entidad JPA para mesas.
 */
@Entity
@Table(name = "mesa")
@SQLDelete(sql = "UPDATE mesa SET deleted = true, active = false WHERE id = ?")
public class MesaJpaEntity {
    @Id
    private UUID id;

    @Column(name = "description", nullable = false, length = 150)
    private String description;

    @Column(name = "seats", nullable = false)
    private int seats;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "occupied", nullable = false)
    private boolean occupied;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
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

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
