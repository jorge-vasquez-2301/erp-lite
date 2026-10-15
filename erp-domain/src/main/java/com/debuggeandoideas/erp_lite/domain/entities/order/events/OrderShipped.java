package com.debuggeandoideas.erp_lite.domain.entities.order.events;

import com.debuggeandoideas.erp_lite.domain.common.DomainEvent;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderId;

import java.time.Instant;

/**
 * Emitted when order transitions CONFIRMED -> SHIPPED.
 *
 * @param orderId   the order identifier
 * @param timestamp the event timestamp
 */
public record OrderShipped(
        OrderId orderId,
        Instant timestamp
) implements DomainEvent {
}
