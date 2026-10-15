package com.debuggeandoideas.erp_lite.commands.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * Command to create a new order.
 *
 * @param customerId Customer ID from external system (JSONPlaceholder)
 * @param items      List of order items (products and quantities)
 * @param createdBy  Username of the user creating the order
 */
public record CreateOrderCommand(

        @NotNull(message = "Customer ID cannot be null")
        @Min(value = 1, message = "Customer ID must be greater than 0")
        Long customerId,

        @NotNull(message = "Order items cannot be null")
        @NotEmpty(message = "Order must have at least one item")
        @Valid
        List<OrderItemRequest> items,

        @NotBlank(message = "Created by cannot be null or blank")
        String createdBy
) {
    /**
     * Custom validation in compact constructor
     */
    public CreateOrderCommand {
        if (items != null && items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
    }

    /**
     * Get total number of items
     */
    public int getTotalItems() {
        return items != null ? items.size() : 0;
    }

    /**
     * Request DTO for order items
     */
    public record OrderItemRequest(

            @NotBlank(message = "Product ID cannot be null or blank")
            String productId,

            @NotNull(message = "Quantity cannot be null")
            @Min(value = 1, message = "Quantity must be at least 1")
            @Max(value = 1000, message = "Quantity cannot exceed 1000 units per item")
            Integer quantity
    ) {
        public OrderItemRequest {
            if (quantity != null && quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
        }
    }
}