package com.debuggeandoideas.erp_lite.domain.entities.product;

/**
 * Product name.
 * Must have between 3 and 200 characters.
 *
 * @param value the product name
 */
public record ProductName(String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 200;

    public ProductName {
        if (value == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Product name must have at least " + MIN_LENGTH + " characters");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Product name cannot exceed " + MAX_LENGTH + " characters");
        }
    }

    /**
     * Creates a ProductName from a String value.
     *
     * @param value the product name
     * @return a new ProductName instance
     */
    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
