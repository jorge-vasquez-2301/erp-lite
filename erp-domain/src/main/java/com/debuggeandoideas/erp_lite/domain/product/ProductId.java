package com.debuggeandoideas.erp_lite.domain.product;

import java.util.UUID;
/**
 * Unique identifier for Product aggregate.
 *
 * @param value the UUID value
 */
public record ProductId(UUID value) {

    public ProductId {
        if (value == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
    }

    /**
     * Creates a ProductId from a UUID value.
     *
     * @param value the UUID value
     * @return a new ProductId instance
     */
    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    /**
     * Generates a new random ProductId.
     *
     * @return a new ProductId instance with a random UUID
     */
    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
}
