package com.pickeat.application.usecase;

import com.pickeat.domain.AppParameter;
import com.pickeat.domain.OrderConfig;
import com.pickeat.domain.TipType;
import com.pickeat.ports.in.GetOrderConfigUseCase;
import com.pickeat.ports.out.ParameterRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Servicio de aplicacion para obtener configuracion de ordenes.
 */
@Service
public class GetOrderConfigService implements GetOrderConfigUseCase {
    private final ParameterRepositoryPort parameterRepository;

    public GetOrderConfigService(ParameterRepositoryPort parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @Override
    public OrderConfig getConfig() {
        BigDecimal taxRate = getNumeric("TAX_RATE", BigDecimal.ZERO);
        String tipTypeText = getText("TIP_TYPE", TipType.PERCENTAGE.name());
        TipType tipType;
        try {
            tipType = TipType.valueOf(tipTypeText);
        } catch (IllegalArgumentException ex) {
            tipType = TipType.PERCENTAGE;
        }
        BigDecimal tipValue = getNumeric("TIP_VALUE", BigDecimal.ZERO);
        String currencyCode = getText("CURRENCY_CODE", "USD");
        String currencySymbol = getText("CURRENCY_SYMBOL", "$");
        return new OrderConfig(taxRate, tipType, tipValue, currencyCode, currencySymbol);
    }

    private BigDecimal getNumeric(String key, BigDecimal fallback) {
        return parameterRepository.findByKey(key)
                .map(AppParameter::getNumericValue)
                .orElse(fallback);
    }

    private String getText(String key, String fallback) {
        return parameterRepository.findByKey(key)
                .map(AppParameter::getTextValue)
                .orElse(fallback);
    }
}
