package com.pickeat.domain;

/**
 * Representa una mesa del restaurante.
 */
public class Mesa {
    private MesaId id;
    private String description;
    private int seats;
    private boolean active;
    private boolean deleted;

    public Mesa(MesaId id, String description, int seats, boolean active, boolean deleted) {
        this.id = id;
        this.description = description;
        this.seats = seats;
        this.active = active;
        this.deleted = deleted;
    }

    public static Mesa createNew(String description, int seats, boolean active) {
        return new Mesa(MesaId.newId(), description, seats, active, false);
    }

    public MesaId getId() {
        return id;
    }

    public void setId(MesaId id) {
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
}
