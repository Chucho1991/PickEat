package com.pickeat.adapters.out.persistence;

import com.pickeat.adapters.out.persistence.entity.DiscountItemJpaEntity;
import com.pickeat.adapters.out.persistence.repository.DiscountItemJpaRepository;
import com.pickeat.domain.DiscountApplyScope;
import com.pickeat.domain.DiscountItem;
import com.pickeat.domain.DiscountItemId;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para descuentos.
 */
@Component
public class DiscountItemPersistenceAdapter implements DiscountItemRepositoryPort {
    private final DiscountItemJpaRepository repository;

    public DiscountItemPersistenceAdapter(DiscountItemJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public DiscountItem save(DiscountItem discountItem) {
        DiscountItemJpaEntity entity = toEntity(discountItem);
        DiscountItemJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<DiscountItem> findById(DiscountItemId id) {
        return repository.findById(id.getValue()).map(this::toDomain);
    }

    @Override
    public Optional<DiscountItem> findByNickname(String nickname) {
        return repository.findByNickname(nickname).map(this::toDomain);
    }

    @Override
    public List<DiscountItem> findAll(Boolean activo, String search, boolean includeDeleted) {
        return repository.findAllFiltered(activo, search, includeDeleted).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<DiscountItem> findCouponGenerators() {
        return repository.findCouponGenerators().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<DiscountItem> findAutoApplyItemDiscounts() {
        return repository.findAutoApplyItemDiscounts().stream()
                .map(this::toDomain)
                .toList();
    }

    private DiscountItemJpaEntity toEntity(DiscountItem discountItem) {
        DiscountItemJpaEntity entity = new DiscountItemJpaEntity();
        entity.setId(discountItem.getId().getValue());
        entity.setLongDescription(discountItem.getLongDescription());
        entity.setShortDescription(discountItem.getShortDescription());
        entity.setNickname(discountItem.getNickname());
        entity.setDiscountType(discountItem.getDiscountType());
        entity.setValue(discountItem.getValue());
        entity.setApplyScope(discountItem.getApplyScope());
        entity.setExclusive(discountItem.isExclusive());
        entity.setApplyOverDiscount(discountItem.isApplyOverDiscount());
        entity.setAutoApply(discountItem.isAutoApply());
        entity.setGeneratesCoupon(discountItem.isGeneratesCoupon());
        entity.setCouponRuleType(discountItem.getCouponRuleType());
        entity.setCouponMinTotal(discountItem.getCouponMinTotal());
        entity.setCouponDishType(discountItem.getCouponDishType());
        entity.setCouponMinItemQty(discountItem.getCouponMinItemQty());
        entity.setCouponValidityDays(discountItem.getCouponValidityDays());
        entity.setCouponRequireNoDiscount(discountItem.isCouponRequireNoDiscount());
        entity.setCouponActive(discountItem.isCouponActive());
        entity.setMenuItemIds(discountItem.getMenuItemIds());
        entity.setActive(discountItem.isActive());
        entity.setDeleted(discountItem.isDeleted());
        entity.setImagePath(discountItem.getImagePath());
        entity.setCreatedAt(discountItem.getCreatedAt());
        entity.setUpdatedAt(discountItem.getUpdatedAt());
        return entity;
    }

    private DiscountItem toDomain(DiscountItemJpaEntity entity) {
        return new DiscountItem(
                new DiscountItemId(entity.getId()),
                entity.getLongDescription(),
                entity.getShortDescription(),
                entity.getNickname(),
                entity.getDiscountType(),
                entity.getValue(),
                entity.getApplyScope() == null ? DiscountApplyScope.ORDER : entity.getApplyScope(),
                entity.isExclusive(),
                entity.isApplyOverDiscount(),
                entity.isAutoApply(),
                entity.isGeneratesCoupon(),
                entity.getCouponRuleType(),
                entity.getCouponMinTotal(),
                entity.getCouponDishType(),
                entity.getCouponMinItemQty(),
                entity.getCouponValidityDays(),
                entity.isCouponRequireNoDiscount(),
                entity.isCouponActive(),
                entity.getMenuItemIds(),
                entity.isActive(),
                entity.isDeleted(),
                entity.getImagePath(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
