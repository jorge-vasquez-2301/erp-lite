package com.debuggeandoideas.erp_lite.commands.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Command to update product stock.
 * Can be used for both increment (positive quantity) and decrement (negative quantity).
 *
 * @param productId Product ID to update stock
 * @param quantity  Quantity to add (positive) or subtract (negative)
 * @param reason    Reason for stock update (e.g., "PURCHASE", "SALE", "ADJUSTMENT", "DAMAGED")
 */
public record UpdateStockCommand(

        @NotBlank(message = "Product ID cannot be null or blank")
        String productId,

        @NotNull(message = "Quantity cannot be null")
        Integer quantity,

        @NotBlank(message = "Reason cannot be null or blank")
        @Size(min = 3, max = 100, message = "Reason must be between 3 and 100 characters")
        String reason
) {
    /**
     * Custom validation: quantity cannot be zero
     */
    public UpdateStockCommand {
        if (quantity != null && quantity == 0) {
            throw new IllegalArgumentException("Quantity cannot be zero");
        }
    }

    /**
     * Check if this is an increment operation.
     */
    public boolean isIncrement() {
        return quantity != null && quantity > 0;
    }

    /**
     * Check if this is a decrement operation.
     */
    public boolean isDecrement() {
        return quantity != null && quantity < 0;
    }

    /**
     * Get absolute quantity value.
     */
    public int absoluteQuantity() {
        return quantity != null ? Math.abs(quantity) : 0;
    }
}