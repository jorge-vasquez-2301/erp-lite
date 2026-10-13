package com.debuggeandoideas.erp_lite.domain.product;

import java.util.regex.Pattern;

/**
 * Stock Keeping Unit. Unique and immutable.
 * Pattern: [A-Z]+-\d{3} (e.g., LAPTOP-001, MOUSE-042)
 *
 * @param value the SKU value
 */
public record SKU(String value) {

    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z]+-\\d{3}$");

    public SKU {
        if (value == null) {
            throw new IllegalArgumentException("SKU cannot be null");
        }
        if (!SKU_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid SKU format. Expected pattern: [A-Z]+-\\d{3}, got: " + value);
        }
    }

    /**
     * Creates a SKU from a String value.
     *
     * @param value the SKU value
     * @return a new SKU instance
     */
    public static SKU of(String value) {
        return new SKU(value);
    }
}
