package com.pickeat.application.usecase;

import com.pickeat.domain.OrderChannel;
import com.pickeat.ports.in.ListOrderChannelsUseCase;
import com.pickeat.ports.out.OrderChannelRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Servicio de aplicacion para listar canales de orden.
 */
@Service
public class ListOrderChannelsService implements ListOrderChannelsUseCase {
    private final OrderChannelRepositoryPort repository;

    public ListOrderChannelsService(OrderChannelRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<OrderChannel> list(boolean includeDeleted) {
        return repository.findAll(includeDeleted).stream()
                .sorted(Comparator.comparing(OrderChannel::isDefault).reversed()
                        .thenComparing(OrderChannel::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
