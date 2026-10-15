package com.debuggeandoideas.erp_lite.commands.helpers;

import com.debuggeandoideas.erp_lite.domain.entities.order.OrderId;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderRoot;
import com.debuggeandoideas.erp_lite.domain.ports.repositories.OrderRepositoryPort;
import com.debuggeandoideas.erp_lite.exceptions.CommandException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommandHelper {

    private final OrderRepositoryPort orderRepository;


    public OrderRoot findOrderById(String orderId) {
        log.info("Finding order by ID: {}", orderId);

        return this.orderRepository.findById(OrderId.of(UUID.fromString(orderId)))
                .orElseThrow(() -> new CommandException("Order not found"));
    }
}