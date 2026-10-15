package com.debuggeandoideas.erp_lite.domain.entities.order.events;

import com.debuggeandoideas.erp_lite.domain.common.DomainEvent;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderId;

import java.time.Instant;

/**
 * Emitted when order transitions PENDING -> CONFIRMED.
 * TRIGGERS stock decrement.
 *
 * @param orderId   the order identifier
 * @param timestamp the event timestamp
 */
public record OrderConfirmed(
        OrderId orderId,
        Instant timestamp
) implements DomainEvent {
}
