package com.debuggeandoideas.erp_lite.domain.entities.order.events;

import com.debuggeandoideas.erp_lite.domain.common.DomainEvent;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderId;
import com.debuggeandoideas.erp_lite.domain.shared.CustomerId;
import com.debuggeandoideas.erp_lite.domain.shared.Money;

import java.time.Instant;

/**
 * Emitted when a new order is created.
 *
 * @param orderId      the order identifier
 * @param customerId   the customer identifier
 * @param customerName the customer name
 * @param totalAmount  the total order amount
 * @param timestamp    the event timestamp
 */
public record OrderCreated(
        OrderId orderId,
        CustomerId customerId,
        String customerName,
        Money totalAmount,
        Instant timestamp
) implements DomainEvent {
}
