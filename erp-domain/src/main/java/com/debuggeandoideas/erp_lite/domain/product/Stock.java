package com.debuggeandoideas.erp_lite.domain.product;

/**
 * Product stock quantity. Cannot be negative.
 *
 * @param value the stock value (must be >= 0)
 */
public record Stock(Integer value) {

    public Stock {
        if (value == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }

    /**
     * Creates a Stock from an integer value.
     *
     * @param value the stock value
     * @return a new Stock instance
     */
    public static Stock of(int value) {
        return new Stock(value);
    }

    /**
     * Creates a Stock with zero value.
     *
     * @return a new Stock instance with value 0
     */
    public static Stock zero() {
        return new Stock(0);
    }

    /**
     * Increments the stock by the specified quantity.
     *
     * @param quantity the quantity to add
     * @return a new Stock instance with the incremented value
     */
    public Stock increment(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Increment quantity cannot be negative");
        }
        return new Stock(this.value + quantity);
    }

    /**
     * Decrements the stock by the specified quantity.
     *
     * @param quantity the quantity to subtract
     * @return a new Stock instance with the decremented value
     * @throws IllegalArgumentException if the result would be negative
     */
    public Stock decrement(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Decrement quantity cannot be negative");
        }
        int newValue = this.value - quantity;
        if (newValue < 0) {
            throw new IllegalArgumentException("Cannot decrement stock below zero. Current: " + this.value + ", requested: " + quantity);
        }
        return new Stock(newValue);
    }

    /**
     * Checks if the stock has the required quantity available.
     *
     * @param required the required quantity
     * @return true if available stock is greater than or equal to required
     */
    public boolean hasAvailable(int required) {
        return this.value >= required;
    }
}
