package com.debuggeandoideas.erp_lite.domain.entities.order.events;

import com.debuggeandoideas.erp_lite.domain.common.DomainEvent;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderId;

import java.time.Instant;

/**
 * Emitted when order is cancelled.
 * If was CONFIRMED, stock must be released.
 *
 * @param orderId   the order identifier
 * @param reason    the cancellation reason
 * @param timestamp the event timestamp
 */
public record OrderCancelled(
        OrderId orderId,
        String reason,
        Instant timestamp
) implements DomainEvent {
}
