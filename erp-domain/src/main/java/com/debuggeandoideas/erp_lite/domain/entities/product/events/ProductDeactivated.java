package com.debuggeandoideas.erp_lite.domain.entities.product.events;

import com.debuggeandoideas.erp_lite.domain.common.DomainEvent;
import com.debuggeandoideas.erp_lite.domain.entities.product.ProductId;

import java.time.Instant;

/**
 * Emitted when product is deactivated.
 * TRIGGERS sync to MongoDB.
 *
 * @param productId the product identifier
 * @param timestamp the event timestamp
 */
public record ProductDeactivated(
        ProductId productId,
        Instant timestamp
) implements DomainEvent {
}
