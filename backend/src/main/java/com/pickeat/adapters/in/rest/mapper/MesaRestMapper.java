package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.MesaRequest;
import com.pickeat.adapters.in.rest.dto.MesaResponse;
import com.pickeat.domain.Mesa;

/**
 * Mapper REST para mesas.
 */
public class MesaRestMapper {
    public Mesa toDomain(MesaRequest request) {
        Mesa mesa = Mesa.createNew(
                request.getDescription(),
                request.getSeats(),
                Boolean.TRUE.equals(request.getActivo())
        );
        mesa.setOccupied(Boolean.TRUE.equals(request.getOcupada()));
        return mesa;
    }

    public Mesa toUpdateDomain(MesaRequest request) {
        return new Mesa(
                null,
                request.getDescription(),
                request.getSeats(),
                Boolean.TRUE.equals(request.getActivo()),
                false,
                Boolean.TRUE.equals(request.getOcupada())
        );
    }

    public MesaResponse toResponse(Mesa mesa) {
        return new MesaResponse(
                mesa.getId().getValue(),
                mesa.getDescription(),
                mesa.getSeats(),
                mesa.isActive(),
                mesa.isDeleted(),
                mesa.isOccupied()
        );
    }
}
