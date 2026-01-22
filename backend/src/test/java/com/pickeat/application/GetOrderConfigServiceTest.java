package com.pickeat.application;

import com.pickeat.application.usecase.GetOrderConfigService;
import com.pickeat.domain.AppParameter;
import com.pickeat.domain.OrderConfig;
import com.pickeat.ports.out.ParameterRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GetOrderConfigServiceTest {
    @Test
    void loadsConfigFromParameters() {
        ParameterRepositoryPort parameterRepository = Mockito.mock(ParameterRepositoryPort.class);
        GetOrderConfigService service = new GetOrderConfigService(parameterRepository);

        when(parameterRepository.findByKey("TAX_RATE")).thenReturn(Optional.of(new AppParameter("TAX_RATE", BigDecimal.valueOf(12), null, null)));
        when(parameterRepository.findByKey("TIP_TYPE")).thenReturn(Optional.of(new AppParameter("TIP_TYPE", null, "FIXED", null)));
        when(parameterRepository.findByKey("TIP_VALUE")).thenReturn(Optional.of(new AppParameter("TIP_VALUE", BigDecimal.valueOf(3.50), null, null)));
        when(parameterRepository.findByKey("CURRENCY_CODE")).thenReturn(Optional.of(new AppParameter("CURRENCY_CODE", null, "COP", null)));
        when(parameterRepository.findByKey("CURRENCY_SYMBOL")).thenReturn(Optional.of(new AppParameter("CURRENCY_SYMBOL", null, "$", null)));

        OrderConfig config = service.getConfig();

        assertThat(config.getTaxRate()).isEqualTo(BigDecimal.valueOf(12));
        assertThat(config.getTipType().name()).isEqualTo("FIXED");
        assertThat(config.getTipValue()).isEqualTo(BigDecimal.valueOf(3.50));
        assertThat(config.getCurrencyCode()).isEqualTo("COP");
    }
}
