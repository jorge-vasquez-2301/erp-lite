package com.debuggeandoideas.erp_lite.domain.product.events;

import com.debuggeandoideas.erp_lite.domain.common.DomainEvent;
import com.debuggeandoideas.erp_lite.domain.product.ProductId;

import java.time.Instant;

/**
 * Emitted when stock changes (increment or decrement).
 * TRIGGERS sync to MongoDB.
 *
 * @param productId the product identifier
 * @param oldStock  the old stock value
 * @param newStock  the new stock value
 * @param reason    the reason for the stock change
 * @param timestamp the event timestamp
 */
public record StockChanged(
        ProductId productId,
        Integer oldStock,
        Integer newStock,
        String reason,
        Instant timestamp
) implements DomainEvent {
}
