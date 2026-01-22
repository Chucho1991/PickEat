package com.pickeat.application;

import com.pickeat.application.usecase.GetOrderService;
import com.pickeat.domain.Order;
import com.pickeat.domain.OrderId;
import com.pickeat.ports.out.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GetOrderServiceTest {
    @Test
    void returnsOrderById() {
        OrderRepositoryPort orderRepository = Mockito.mock(OrderRepositoryPort.class);
        GetOrderService service = new GetOrderService(orderRepository);
        OrderId orderId = new OrderId(UUID.randomUUID());
        Order order = new Order(
                orderId,
                1L,
                new com.pickeat.domain.MesaId(UUID.randomUUID()),
                List.of(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "USD",
                "$",
                Instant.now(),
                Instant.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order found = service.getById(orderId);

        assertThat(found.getOrderNumber()).isEqualTo(1L);
    }
}
