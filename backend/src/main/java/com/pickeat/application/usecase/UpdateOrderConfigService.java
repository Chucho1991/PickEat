package com.pickeat.application.usecase;

import com.pickeat.domain.AppParameter;
import com.pickeat.domain.OrderConfig;
import com.pickeat.domain.TipType;
import com.pickeat.ports.in.UpdateOrderConfigUseCase;
import com.pickeat.ports.out.ParameterRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Servicio de aplicacion para actualizar configuracion de ordenes.
 */
@Service
public class UpdateOrderConfigService implements UpdateOrderConfigUseCase {
    private final ParameterRepositoryPort parameterRepository;

    public UpdateOrderConfigService(ParameterRepositoryPort parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    /**
     * Actualiza los porcentajes de impuesto y propina.
     *
     * @param taxRate porcentaje de impuesto.
     * @param tipValue porcentaje de propina.
     * @return configuracion actualizada.
     */
    @Override
    public OrderConfig update(BigDecimal taxRate, BigDecimal tipValue) {
        BigDecimal taxRateSafe = defaultIfNull(taxRate);
        BigDecimal tipValueSafe = defaultIfNull(tipValue);
        validatePercentage("Impuesto", taxRateSafe);
        validatePercentage("Propina", tipValueSafe);

        parameterRepository.save(new AppParameter("TAX_RATE", taxRateSafe, null, null));
        parameterRepository.save(new AppParameter("TIP_TYPE", null, TipType.PERCENTAGE.name(), null));
        parameterRepository.save(new AppParameter("TIP_VALUE", tipValueSafe, null, null));

        String currencyCode = parameterRepository.findByKey("CURRENCY_CODE")
                .map(AppParameter::getTextValue)
                .orElse("USD");
        String currencySymbol = parameterRepository.findByKey("CURRENCY_SYMBOL")
                .map(AppParameter::getTextValue)
                .orElse("$");

        return new OrderConfig(
                taxRateSafe,
                TipType.PERCENTAGE,
                tipValueSafe,
                currencyCode,
                currencySymbol
        );
    }

    private void validatePercentage(String label, BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException(label + " fuera de rango (0-100).");
        }
    }

    private BigDecimal defaultIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
