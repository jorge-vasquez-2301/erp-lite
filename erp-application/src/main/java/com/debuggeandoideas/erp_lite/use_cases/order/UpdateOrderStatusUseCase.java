package com.debuggeandoideas.erp_lite.use_cases.order;

import com.debuggeandoideas.erp_lite.commands.helpers.CommandHelper;
import com.debuggeandoideas.erp_lite.commands.order.UpdateOrderStatusCommand;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderRoot;
import com.debuggeandoideas.erp_lite.domain.ports.repositories.OrderRepositoryPort;
import com.debuggeandoideas.erp_lite.exceptions.CommandException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * * JIRA TICKET: ERP-6734
 * Business Rules:
 * - Order must exist
 * - Status transition must be valid (enforced by domain)
 * Valid Transitions:
 * - PENDING → CONFIRMED
 * - CONFIRMED → SHIPPED
 * - SHIPPED → DELIVERED
 * 1. Find order by ID
 * 2. Update status (domain validates transition)
 * 3. Persist order
 *
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {

    private final OrderRepositoryPort orderRepository;
    private final CommandHelper commandHelper;

    public String execute(UpdateOrderStatusCommand command) {

        try {
            OrderRoot orderRoot = this.commandHelper.findOrderById(command.orderId());

            log.info("Current order current status: {}", orderRoot.getStatus());

            this.updateStatus(orderRoot, command.newStatus());

            OrderRoot orderSaved = this.orderRepository.save(orderRoot);

            log.info("Order saved current status: {}", orderSaved.getStatus());

            return orderSaved.getStatus().toString();

        } catch (IllegalStateException ise) {
            log.error("Error updating order status", ise);
            throw new CommandException("Error updating order status");
        } catch (Exception e) {
            log.error("Error updating order status", e);
            throw new CommandException("Unexpected error updating order status");
        }
    }


    private void updateStatus(OrderRoot order, String status) {

        switch (status.toUpperCase()) {
            case "CONFIRMED" -> order.confirm();
            case "SHIPPED" -> order.ship();
            case "DELIVERED" -> order.deliver();
            default -> throw new CommandException("Unexpected value: " + status);
        }
    }

}