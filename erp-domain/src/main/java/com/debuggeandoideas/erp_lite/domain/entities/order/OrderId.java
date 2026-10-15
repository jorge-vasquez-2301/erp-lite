package com.debuggeandoideas.erp_lite.domain.entities.order;

import java.util.UUID;

/**
 * Unique identifier for Order aggregate.
 *
 * @param value the UUID value
 */
public record OrderId(UUID value) {

    public OrderId {
        if (value == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
    }

    /**
     * Creates an OrderId from a UUID value.
     *
     * @param value the UUID value
     * @return a new OrderId instance
     */
    public static OrderId of(UUID value) {
        return new OrderId(value);
    }

    /**
     * Generates a new random OrderId.
     *
     * @return a new OrderId instance with a random UUID
     */
    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
}
