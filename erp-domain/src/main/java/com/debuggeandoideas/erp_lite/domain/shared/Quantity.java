package com.debuggeandoideas.erp_lite.domain.shared;

/**
 * Quantity of items in order. Must be greater than 0.
 *
 * @param value the quantity value (must be > 0)
 */
public record Quantity(Integer value) {

    public Quantity {
        if (value == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    /**
     * Creates a Quantity from an integer value.
     *
     * @param value the quantity value
     * @return a new Quantity instance
     */
    public static Quantity of(int value) {
        return new Quantity(value);
    }
}
