package com.debuggeandoideas.erp_lite.commands.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Command to cancel an order.
 * Can be used to cancel orders in PENDING or CONFIRMED status.
 * If order was CONFIRMED, stock will be released back to inventory.
 *
 * @param orderId Order ID to cancel
 * @param reason  Cancellation reason (for audit purposes)
 */
public record CancelOrderCommand(

        @NotBlank(message = "Order ID cannot be null or blank")
        @Pattern(
                regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                message = "Order ID must be a valid UUID"
        )
        String orderId,

        @NotBlank(message = "Cancellation reason cannot be null or blank")
        @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
        String reason
) {
    /**
     * Custom validation in compact constructor
     */
    public CancelOrderCommand {
        if (reason != null && reason.trim().length() < 10) {
            throw new IllegalArgumentException("Cancellation reason must have at least 10 meaningful characters");
        }
    }

    /**
     * Check if reason mentions customer request
     */
    public boolean isCustomerRequest() {
        return reason != null &&
                (reason.toLowerCase().contains("customer") ||
                        reason.toLowerCase().contains("client"));
    }

    /**
     * Check if reason mentions stock issues
     */
    public boolean isStockIssue() {
        return reason != null &&
                (reason.toLowerCase().contains("stock") ||
                        reason.toLowerCase().contains("inventory") ||
                        reason.toLowerCase().contains("out of stock"));
    }
}