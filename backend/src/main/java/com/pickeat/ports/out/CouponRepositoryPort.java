package com.pickeat.ports.out;

import com.pickeat.domain.Coupon;
import com.pickeat.domain.OrderId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Puerto de salida para cupones.
 */
public interface CouponRepositoryPort {
    /**
     * Guarda un cupon.
     *
     * @param coupon cupon a persistir.
     * @return cupon guardado.
     */
    Coupon save(Coupon coupon);

    /**
     * Busca un cupon por id.
     *
     * @param id identificador del cupon.
     * @return cupon encontrado.
     */
    Optional<Coupon> findById(UUID id);

    /**
     * Busca un cupon por codigo.
     *
     * @param code codigo del cupon.
     * @return cupon encontrado.
     */
    Optional<Coupon> findByCode(String code);

    /**
     * Lista cupones generados por una orden.
     *
     * @param orderId orden generadora.
     * @return cupones generados.
     */
    List<Coupon> findByGeneratedOrderId(OrderId orderId);

    /**
     * Lista cupones aplicados a una orden.
     *
     * @param orderId orden con cupon aplicado.
     * @return cupones aplicados.
     */
    List<Coupon> findByRedeemedOrderId(OrderId orderId);
}
