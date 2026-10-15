package com.debuggeandoideas.erp_lite.domain.entities.order;

import com.debuggeandoideas.erp_lite.domain.shared.CustomerId;

/**
 * Customer reference with basic info.
 * Combines customer ID and name for order context.
 *
 * @param customerId   the customer identifier
 * @param customerName the customer name
 */
public record Customer(
        CustomerId customerId,
        String customerName
) {

    public Customer {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be null or blank");
        }
    }

    /**
     * Creates a Customer from CustomerId and name.
     *
     * @param customerId   the customer identifier
     * @param customerName the customer name
     * @return a new Customer instance
     */
    public static Customer of(CustomerId customerId, String customerName) {
        return new Customer(customerId, customerName);
    }
}
