package com.debuggeandoideas.erp_lite.use_cases.order;

import com.debuggeandoideas.erp_lite.commands.helpers.CommandHelper;
import com.debuggeandoideas.erp_lite.commands.order.CancelOrderCommand;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderRoot;
import com.debuggeandoideas.erp_lite.domain.ports.repositories.OrderRepositoryPort;
import com.debuggeandoideas.erp_lite.exceptions.CommandException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CancelOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final CommandHelper commandHelper;

    public void execute(CancelOrderCommand command) {

        try {
            log.info("Cancel order {}", command.orderId());

            OrderRoot orderRoot = this.commandHelper.findOrderById(command.orderId());

            orderRoot.cancel(command.reason());
            this.orderRepository.save(orderRoot);

            log.info("Order {} cancelled", command.orderId());
        } catch (Exception e) {
            log.error("Error on cancel order", e);
            throw new CommandException("Error on cancel order");
        }
    }

}