package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.MenuItemRequest;
import com.pickeat.adapters.in.rest.dto.MenuItemResponse;
import com.pickeat.domain.MenuItem;

/**
 * Mapper REST para ítems del menú.
 */
public class MenuItemRestMapper {
    /**
     * Convierte una solicitud a dominio.
     *
     * @param request solicitud REST.
     * @return entidad de dominio.
     */
    public MenuItem toDomain(MenuItemRequest request) {
        return MenuItem.createNew(
                request.getLongDescription(),
                request.getShortDescription(),
                request.getNickname(),
                request.getDishType(),
                request.getStatus(),
                request.getPrice()
        );
    }

    /**
     * Copia los datos de la solicitud a un dominio existente.
     *
     * @param request solicitud REST.
     * @return entidad con datos actualizados.
     */
    public MenuItem toUpdateDomain(MenuItemRequest request) {
        return new MenuItem(
                null,
                request.getLongDescription(),
                request.getShortDescription(),
                request.getNickname(),
                request.getDishType(),
                request.getStatus(),
                request.getPrice(),
                null,
                null,
                null
        );
    }

    /**
     * Convierte un dominio a respuesta REST.
     *
     * @param menuItem entidad de dominio.
     * @return respuesta REST.
     */
    public MenuItemResponse toResponse(MenuItem menuItem) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId().getValue());
        response.setLongDescription(menuItem.getLongDescription());
        response.setShortDescription(menuItem.getShortDescription());
        response.setNickname(menuItem.getNickname());
        response.setDishType(menuItem.getDishType());
        response.setStatus(menuItem.getStatus());
        response.setPrice(menuItem.getPrice());
        response.setImagePath(menuItem.getImagePath());
        response.setCreatedAt(menuItem.getCreatedAt());
        response.setUpdatedAt(menuItem.getUpdatedAt());
        return response;
    }
}
