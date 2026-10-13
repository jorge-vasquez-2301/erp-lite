package com.debuggeandoideas.erp_lite.domain.shared;

/**
 * Reference to external customer system (JSONPlaceholder).
 * Represents a unique identifier for a customer.
 *
 * @param value the customer ID value (must be > 0)
 */
public record CustomerId(Long value) {

    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
    }

    /**
     * Creates a CustomerId from a Long value.
     *
     * @param value the customer ID value
     * @return a new CustomerId instance
     */
    public static CustomerId of(Long value) {
        return new CustomerId(value);
    }
}
