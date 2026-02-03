package com.pickeat.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para crear o actualizar campo de facturacion.
 */
public class OrderBillingFieldRequest {
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 120, message = "El nombre no puede exceder 120 caracteres.")
    private String label;

    private Integer sortOrder;

    private Boolean active;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
