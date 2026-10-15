package com.debuggeandoideas.erp_lite.commands.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Command to update order status.
 * Valid transitions:
 * - PENDING -> CONFIRMED
 * - CONFIRMED -> SHIPPED
 * - SHIPPED -> DELIVERED
 * <p>
 * NOTE: For CANCELLING an order, use CancelOrderCommand which requires a reason.
 *
 * @param orderId   Order ID to update
 * @param newStatus New status to set (CONFIRMED, SHIPPED, or DELIVERED)
 */
public record UpdateOrderStatusCommand(

        @NotBlank(message = "Order ID cannot be null or blank")
        @Pattern(
                regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                message = "Order ID must be a valid UUID"
        )
        String orderId,

        @NotBlank(message = "New status cannot be null or blank")
        @Pattern(
                regexp = "^(CONFIRMED|SHIPPED|DELIVERED)$",
                message = "Status must be one of: CONFIRMED, SHIPPED, DELIVERED. Use CancelOrderCommand to cancel an order."
        )
        String newStatus
) {
    // Valid statuses (CANCELLED excluded - use CancelOrderCommand)
    private static final String CONFIRMED = "CONFIRMED";
    private static final String SHIPPED = "SHIPPED";
    private static final String DELIVERED = "DELIVERED";

    /**
     * Check if new status is CONFIRMED
     */
    public boolean isConfirming() {
        return CONFIRMED.equals(newStatus);
    }

    /**
     * Check if new status is SHIPPED
     */
    public boolean isShipping() {
        return SHIPPED.equals(newStatus);
    }

    /**
     * Check if new status is DELIVERED
     */
    public boolean isDelivering() {
        return DELIVERED.equals(newStatus);
    }
}