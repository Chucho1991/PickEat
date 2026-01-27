package com.pickeat.adapters.in.rest.mapper;

import com.pickeat.adapters.in.rest.dto.MenuItemRequest;
import com.pickeat.adapters.in.rest.dto.MenuItemResponse;
import com.pickeat.domain.MenuItem;

/**
 * Mapper REST para items del menu.
 */
public class MenuItemRestMapper {
    public MenuItem toDomain(MenuItemRequest request) {
        return MenuItem.createNew(
                request.getLongDescription(),
                request.getShortDescription(),
                request.getNickname(),
                request.getDishType(),
                Boolean.TRUE.equals(request.getActivo()),
                Boolean.TRUE.equals(request.getAplicaImpuesto()),
                request.getPrice()
        );
    }

    public MenuItem toUpdateDomain(MenuItemRequest request) {
        return new MenuItem(
                null,
                request.getLongDescription(),
                request.getShortDescription(),
                request.getNickname(),
                request.getDishType(),
                Boolean.TRUE.equals(request.getActivo()),
                false,
                Boolean.TRUE.equals(request.getAplicaImpuesto()),
                request.getPrice(),
                null,
                null,
                null
        );
    }

    public MenuItemResponse toResponse(MenuItem menuItem) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId().getValue());
        response.setLongDescription(menuItem.getLongDescription());
        response.setShortDescription(menuItem.getShortDescription());
        response.setNickname(menuItem.getNickname());
        response.setDishType(menuItem.getDishType());
        response.setActivo(menuItem.isActive());
        response.setDeleted(menuItem.isDeleted());
        response.setAplicaImpuesto(menuItem.isApplyTax());
        response.setPrice(menuItem.getPrice());
        response.setImagePath(menuItem.getImagePath());
        response.setCreatedAt(menuItem.getCreatedAt());
        response.setUpdatedAt(menuItem.getUpdatedAt());
        return response;
    }
}
