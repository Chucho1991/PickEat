package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.CouponJpaEntity;
import com.pickeat.adapters.out.persistence.repository.CouponJpaRepository;
import com.pickeat.domain.Coupon;
import com.pickeat.domain.CouponStatus;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.domain.OrderId;
import com.pickeat.ports.out.CouponRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador de persistencia para cupones.
 */
@Component
public class CouponPersistenceAdapter implements CouponRepositoryPort {
    private final CouponJpaRepository repository;

    public CouponPersistenceAdapter(CouponJpaRepository repository) {
        this.repository = repository;
    }

    /**
     * Guarda un cupon.
     *
     * @param coupon cupon a persistir.
     * @return cupon guardado.
     */
    @Override
    public Coupon save(Coupon coupon) {
        CouponJpaEntity entity = toEntity(coupon);
        CouponJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    /**
     * Busca un cupon por id.
     *
     * @param id identificador del cupon.
     * @return cupon encontrado.
     */
    @Override
    public Optional<Coupon> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    /**
     * Busca un cupon por codigo.
     *
     * @param code codigo del cupon.
     * @return cupon encontrado.
     */
    @Override
    public Optional<Coupon> findByCode(String code) {
        return repository.findByCode(code).map(this::toDomain);
    }

    /**
     * Lista cupones generados por una orden.
     *
     * @param orderId orden generadora.
     * @return cupones generados.
     */
    @Override
    public List<Coupon> findByGeneratedOrderId(OrderId orderId) {
        return repository.findAllByGeneratedOrderId(orderId.getValue()).stream()
                .map(this::toDomain)
                .toList();
    }

    /**
     * Lista cupones aplicados a una orden.
     *
     * @param orderId orden con cupon aplicado.
     * @return cupones aplicados.
     */
    @Override
    public List<Coupon> findByRedeemedOrderId(OrderId orderId) {
        return repository.findAllByRedeemedOrderId(orderId.getValue()).stream()
                .map(this::toDomain)
                .toList();
    }

    private CouponJpaEntity toEntity(Coupon coupon) {
        CouponJpaEntity entity = new CouponJpaEntity();
        entity.setId(coupon.getId());
        entity.setCode(coupon.getCode());
        entity.setDiscountItemId(coupon.getDiscountItemId().getValue());
        entity.setStatus(coupon.getStatus().name());
        entity.setExpiresAt(coupon.getExpiresAt());
        entity.setGeneratedOrderId(coupon.getGeneratedOrderId() == null ? null : coupon.getGeneratedOrderId().getValue());
        entity.setRedeemedOrderId(coupon.getRedeemedOrderId() == null ? null : coupon.getRedeemedOrderId().getValue());
        entity.setRedeemedAt(coupon.getRedeemedAt());
        entity.setCreatedAt(coupon.getCreatedAt());
        entity.setUpdatedAt(coupon.getUpdatedAt());
        return entity;
    }

    private Coupon toDomain(CouponJpaEntity entity) {
        CouponStatus status = CouponStatus.ACTIVE;
        try {
            status = CouponStatus.valueOf(entity.getStatus());
        } catch (IllegalArgumentException ignored) {
            status = CouponStatus.ACTIVE;
        }
        return new Coupon(
                entity.getId(),
                entity.getCode(),
                new DiscountItemId(entity.getDiscountItemId()),
                status,
                entity.getExpiresAt(),
                entity.getGeneratedOrderId() == null ? null : new OrderId(entity.getGeneratedOrderId()),
                entity.getRedeemedOrderId() == null ? null : new OrderId(entity.getRedeemedOrderId()),
                entity.getRedeemedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
