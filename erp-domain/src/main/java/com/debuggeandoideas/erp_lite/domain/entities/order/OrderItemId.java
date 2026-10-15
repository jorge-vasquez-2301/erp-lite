package com.debuggeandoideas.erp_lite.domain.entities.order;

import java.util.UUID;

/**
 * Unique identifier for OrderItem entity.
 *
 * @param value the UUID value
 */
public record OrderItemId(UUID value) {

    public OrderItemId {
        if (value == null) {
            throw new IllegalArgumentException("OrderItem ID cannot be null");
        }
    }

    /**
     * Creates an OrderItemId from a UUID value.
     *
     * @param value the UUID value
     * @return a new OrderItemId instance
     */
    public static OrderItemId of(UUID value) {
        return new OrderItemId(value);
    }

    /**
     * Generates a new random OrderItemId.
     *
     * @return a new OrderItemId instance with a random UUID
     */
    public static OrderItemId generate() {
        return new OrderItemId(UUID.randomUUID());
    }
}
