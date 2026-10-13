package com.debuggeandoideas.erp_lite.domain.order;

import java.util.Set;

/**
 * Order state with valid transitions.
 * Valid states: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
 * DELIVERED and CANCELLED are final states.
 *
 * @param value the status value
 */
public record OrderStatus(String value) {

    public static final String PENDING = "PENDING";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String SHIPPED = "SHIPPED";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";

    private static final Set<String> VALID_STATUSES = Set.of(
            PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    );

    private static final Set<String> FINAL_STATES = Set.of(DELIVERED, CANCELLED);

    public OrderStatus {
        if (value == null) {
            throw new IllegalArgumentException("Order status cannot be null");
        }
        if (!VALID_STATUSES.contains(value)) {
            throw new IllegalArgumentException("Invalid order status: " + value + ". Valid values: " + VALID_STATUSES);
        }
    }

    /**
     * Creates an OrderStatus from a String value.
     *
     * @param value the status value
     * @return a new OrderStatus instance
     */
    public static OrderStatus of(String value) {
        return new OrderStatus(value);
    }

    public static OrderStatus pending() {
        return new OrderStatus(PENDING);
    }

    public static OrderStatus confirmed() {
        return new OrderStatus(CONFIRMED);
    }

    public static OrderStatus shipped() {
        return new OrderStatus(SHIPPED);
    }

    public static OrderStatus delivered() {
        return new OrderStatus(DELIVERED);
    }

    public static OrderStatus cancelled() {
        return new OrderStatus(CANCELLED);
    }

    /**
     * Validates if this status can transition to the next status.
     * Valid transitions:
     * - PENDING -> CONFIRMED | CANCELLED
     * - CONFIRMED -> SHIPPED | CANCELLED
     * - SHIPPED -> DELIVERED
     * - DELIVERED -> (no transitions, final state)
     * - CANCELLED -> (no transitions, final state)
     *
     * @param nextStatus the target status
     * @return true if the transition is valid
     */
    public boolean canTransitionTo(OrderStatus nextStatus) {
        return switch (this.value) {
            case PENDING -> CONFIRMED.equals(nextStatus.value) || CANCELLED.equals(nextStatus.value);
            case CONFIRMED -> SHIPPED.equals(nextStatus.value) || CANCELLED.equals(nextStatus.value);
            case SHIPPED -> DELIVERED.equals(nextStatus.value);
            case DELIVERED, CANCELLED -> false; // Final states
            default -> false;
        };
    }

    public boolean isPending() {
        return PENDING.equals(this.value);
    }

    public boolean isConfirmed() {
        return CONFIRMED.equals(this.value);
    }

    public boolean isShipped() {
        return SHIPPED.equals(this.value);
    }

    public boolean isDelivered() {
        return DELIVERED.equals(this.value);
    }

    public boolean isCancelled() {
        return CANCELLED.equals(this.value);
    }

    public boolean isFinalState() {
        return FINAL_STATES.contains(this.value);
    }
}
