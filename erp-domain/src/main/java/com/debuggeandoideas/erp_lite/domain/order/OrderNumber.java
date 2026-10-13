package com.debuggeandoideas.erp_lite.domain.order;

import java.time.Year;
import java.util.regex.Pattern;

/**
 * Unique order number.
 * Pattern: ORD-YYYY-NNN (e.g., ORD-2025-001)
 *
 * @param value the order number value
 */
public record OrderNumber(String value) {

    private static final Pattern ORDER_NUMBER_PATTERN = Pattern.compile("^ORD-\\d{4}-\\d{3}$");

    public OrderNumber {
        if (value == null) {
            throw new IllegalArgumentException("Order number cannot be null");
        }
        if (!ORDER_NUMBER_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid order number format. Expected pattern: ORD-YYYY-NNN, got: " + value);
        }
    }

    /**
     * Creates an OrderNumber from a String value.
     *
     * @param value the order number value
     * @return a new OrderNumber instance
     */
    public static OrderNumber of(String value) {
        return new OrderNumber(value);
    }

    /**
     * Generates a new OrderNumber with the current year and a sequence number.
     * Note: In a real implementation, the sequence should be retrieved from a database sequence or counter.
     *
     * @param sequence the sequence number (001-999)
     * @return a new OrderNumber instance
     */
    public static OrderNumber generate(int sequence) {
        if (sequence < 1 || sequence > 999) {
            throw new IllegalArgumentException("Sequence must be between 1 and 999");
        }
        int currentYear = Year.now().getValue();
        String orderNumber = String.format("ORD-%d-%03d", currentYear, sequence);
        return new OrderNumber(orderNumber);
    }
}
