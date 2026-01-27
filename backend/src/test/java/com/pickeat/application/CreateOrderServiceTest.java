package com.pickeat.application;

import com.pickeat.application.usecase.CreateOrderService;
import com.pickeat.domain.AppParameter;
import com.pickeat.domain.DishType;
import com.pickeat.domain.MenuItem;
import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderDraft;
import com.pickeat.domain.OrderItemDraft;
import com.pickeat.domain.OrderChannel;
import com.pickeat.domain.OrderChannelId;
import com.pickeat.ports.out.DiscountItemRepositoryPort;
import com.pickeat.ports.out.MenuItemRepositoryPort;
import com.pickeat.ports.out.MesaRepositoryPort;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import com.pickeat.ports.out.OrderRepositoryPort;
import com.pickeat.ports.out.ParameterRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateOrderServiceTest {
    @Test
    void createsOrderWithCalculatedTotals() {
        OrderRepositoryPort orderRepository = Mockito.mock(OrderRepositoryPort.class);
        MesaRepositoryPort mesaRepository = Mockito.mock(MesaRepositoryPort.class);
        MenuItemRepositoryPort menuItemRepository = Mockito.mock(MenuItemRepositoryPort.class);
        DiscountItemRepositoryPort discountItemRepository = Mockito.mock(DiscountItemRepositoryPort.class);
        ParameterRepositoryPort parameterRepository = Mockito.mock(ParameterRepositoryPort.class);
        OrderChannelRepositoryPort orderChannelRepository = Mockito.mock(OrderChannelRepositoryPort.class);
        CreateOrderService service = new CreateOrderService(
                orderRepository,
                mesaRepository,
                menuItemRepository,
                discountItemRepository,
                parameterRepository,
                orderChannelRepository
        );

        Mesa mesa = new Mesa(new MesaId(UUID.randomUUID()), "Mesa 1", 4, true, false, false);
        MenuItem menuItem = MenuItem.createNew(
                "Hamburguesa artesanal con doble queso",
                "Hamburguesa doble",
                "hamburguesa-doble",
                DishType.FUERTE,
                true,
                true,
                BigDecimal.valueOf(10.00)
        );
        OrderDraft draft = new OrderDraft(
                mesa.getId(),
                List.of(new OrderItemDraft(menuItem.getId(), 2)),
                null,
                null,
                null,
                null,
                true
        );
        OrderChannel channel = new OrderChannel(
                new OrderChannelId(UUID.randomUUID()),
                "LOCAL",
                true,
                false,
                true,
                true,
                java.time.Instant.now(),
                java.time.Instant.now()
        );

        when(mesaRepository.findById(mesa.getId())).thenReturn(Optional.of(mesa));
        when(menuItemRepository.findById(menuItem.getId())).thenReturn(Optional.of(menuItem));
        when(orderChannelRepository.findDefault()).thenReturn(Optional.of(channel));
        when(parameterRepository.findByKey("TAX_RATE")).thenReturn(Optional.of(new AppParameter("TAX_RATE", BigDecimal.valueOf(10), null, null)));
        when(parameterRepository.findByKey("TIP_TYPE")).thenReturn(Optional.of(new AppParameter("TIP_TYPE", null, "PERCENTAGE", null)));
        when(parameterRepository.findByKey("TIP_VALUE")).thenReturn(Optional.of(new AppParameter("TIP_VALUE", BigDecimal.valueOf(5), null, null)));
        when(parameterRepository.findByKey("CURRENCY_CODE")).thenReturn(Optional.of(new AppParameter("CURRENCY_CODE", null, "USD", null)));
        when(parameterRepository.findByKey("CURRENCY_SYMBOL")).thenReturn(Optional.of(new AppParameter("CURRENCY_SYMBOL", null, "$", null)));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = service.create(draft);

        assertThat(order.getSubtotal()).isEqualTo(BigDecimal.valueOf(20.00).setScale(2));
        assertThat(order.getTaxAmount()).isEqualTo(BigDecimal.valueOf(2.00).setScale(2));
        assertThat(order.getTipAmount()).isEqualTo(BigDecimal.valueOf(1.10).setScale(2));
        assertThat(order.getTotalAmount()).isEqualTo(BigDecimal.valueOf(23.10).setScale(2));
    }
}
