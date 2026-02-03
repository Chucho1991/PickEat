package com.pickeat.ports.in;

import com.pickeat.domain.Coupon;
import com.pickeat.domain.OrderId;

import java.util.List;

/**
 * Caso de uso para listar cupones asociados a una orden.
 */
public interface ListOrderCouponsUseCase {
    List<Coupon> listByOrder(OrderId orderId);
}
