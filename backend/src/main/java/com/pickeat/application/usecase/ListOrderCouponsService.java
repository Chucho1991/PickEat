package com.pickeat.application.usecase;

import com.pickeat.domain.Coupon;
import com.pickeat.domain.OrderId;
import com.pickeat.ports.in.ListOrderCouponsUseCase;
import com.pickeat.ports.out.CouponRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para listar cupones asociados a una orden.
 */
@Service
public class ListOrderCouponsService implements ListOrderCouponsUseCase {
    private final CouponRepositoryPort couponRepository;

    public ListOrderCouponsService(CouponRepositoryPort couponRepository) {
        this.couponRepository = couponRepository;
    }

    /**
     * Lista cupones generados o aplicados en una orden.
     *
     * @param orderId identificador de la orden.
     * @return cupones asociados.
     */
    @Override
    public List<Coupon> listByOrder(OrderId orderId) {
        List<Coupon> result = new ArrayList<>();
        result.addAll(couponRepository.findByGeneratedOrderId(orderId));
        result.addAll(couponRepository.findByRedeemedOrderId(orderId));
        return result;
    }
}
