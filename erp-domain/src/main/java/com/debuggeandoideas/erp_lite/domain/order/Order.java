package com.debuggeandoideas.erp_lite.domain.order;

import com.debuggeandoideas.erp_lite.domain.common.AggregateRoot;
import com.debuggeandoideas.erp_lite.domain.order.events.*;
import com.debuggeandoideas.erp_lite.domain.shared.AuditInfo;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order Aggregate Root.
 * Manages order lifecycle, items, and state transitions.
 */
@Getter
public class Order extends AggregateRoot<OrderId> {

    private OrderNumber orderNumber;
    private Customer customer;
    private OrderStatus status;
    private List<OrderItem> items;
    private Money totalAmount;
    private AuditInfo auditInfo;

    protected Order() {
        super(null);
    }

    private Order(
            OrderId id,
            OrderNumber orderNumber,
            Customer customer,
            OrderStatus status,
            List<OrderItem> items,
            Money totalAmount,
            AuditInfo auditInfo
    ) {
        super(id);
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.status = status;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.auditInfo = auditInfo;
    }

    /**
     * Creates a new Order.
     *
     * @param orderNumber the unique order number
     * @param customer    the customer information
     * @param items       the list of order items (must not be empty)
     * @param createdBy   the user who created the order
     * @return a new Order instance in PENDING status
     */
    public static Order create(
            OrderNumber orderNumber,
            Customer customer,
            List<OrderItem> items,
            String createdBy
    ) {
        if (orderNumber == null) {
            throw new IllegalArgumentException("Order number cannot be null");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        validateItems(items);

        OrderId orderId = OrderId.generate();
        OrderStatus status = OrderStatus.pending();
        Money totalAmount = calculateTotal(items);
        Instant now = Instant.now();
        AuditInfo auditInfo = AuditInfo.create(createdBy, now);

        Order order = new Order(
                orderId,
                orderNumber,
                customer,
                status,
                items,
                totalAmount,
                auditInfo
        );

        order.registerEvent(new OrderCreated(
                orderId,
                customer.customerId(),
                customer.customerName(),
                totalAmount,
                now
        ));

        return order;
    }

    /**
     * Confirms the order (PENDING -> CONFIRMED).
     * This transition triggers stock decrement.
     */
    public void confirm() {
        OrderStatus nextStatus = OrderStatus.confirmed();
        validateTransition(nextStatus);

        this.status = nextStatus;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new OrderConfirmed(this.id, Instant.now()));
    }

    /**
     * Ships the order (CONFIRMED -> SHIPPED).
     */
    public void ship() {
        OrderStatus nextStatus = OrderStatus.shipped();
        validateTransition(nextStatus);

        this.status = nextStatus;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new OrderShipped(this.id, Instant.now()));
    }

    /**
     * Delivers the order (SHIPPED -> DELIVERED).
     * DELIVERED is a final state.
     */
    public void deliver() {
        OrderStatus nextStatus = OrderStatus.delivered();
        validateTransition(nextStatus);

        this.status = nextStatus;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new OrderDelivered(this.id, Instant.now()));
    }

    /**
     * Cancels the order.
     * If order was CONFIRMED, stock must be released.
     *
     * @param reason the cancellation reason
     */
    public void cancel(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Cancellation reason cannot be null or blank");
        }

        OrderStatus nextStatus = OrderStatus.cancelled();
        validateTransition(nextStatus);

        this.status = nextStatus;
        this.auditInfo = this.auditInfo.updateTimestamp();

        registerEvent(new OrderCancelled(this.id, reason, Instant.now()));
    }

    /**
     * Adds an item to the order.
     * Only allowed in PENDING status.
     *
     * @param item the item to add
     */
    public void addItem(OrderItem item) {
        if (!this.status.isPending()) {
            throw new IllegalStateException("Cannot add items to order in status: " + this.status.value());
        }
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }

        this.items.add(item);
        this.totalAmount = calculateTotal(this.items);
        this.auditInfo = this.auditInfo.updateTimestamp();
    }

    /**
     * Removes an item from the order.
     * Only allowed in PENDING status.
     *
     * @param item the item to remove
     */
    public void removeItem(OrderItem item) {
        if (!this.status.isPending()) {
            throw new IllegalStateException("Cannot remove items from order in status: " + this.status.value());
        }
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        if (!this.items.remove(item)) {
            throw new IllegalArgumentException("Item not found in order");
        }
        if (this.items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }

        this.totalAmount = calculateTotal(this.items);
        this.auditInfo = this.auditInfo.updateTimestamp();
    }

    /**
     * Returns an unmodifiable copy of the order items.
     *
     * @return the list of order items
     */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Calculates the total amount by summing all item subtotals.
     *
     * @return the total amount
     */
    private static Money calculateTotal(List<OrderItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot calculate total for empty order");
        }

        Money total = items.get(0).getSubtotal();
        for (int i = 1; i < items.size(); i++) {
            total = total.add(items.get(i).getSubtotal());
        }

        return total;
    }

    /**
     * Validates that all items are valid.
     * - All items must have the same currency
     * - Each item's subtotal must equal quantity * unitPrice
     */
    private static void validateItems(List<OrderItem> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }

        java.util.Currency firstCurrency = items.get(0).getUnitPrice().currency();

        items.forEach(item -> {
            if (!item.getUnitPrice().currency().equals(firstCurrency)) {
                throw new IllegalArgumentException(
                        "All items must have the same currency. Expected: " + firstCurrency +
                                ", found: " + item.getUnitPrice().currency()
                );
            }

            // Validate subtotal calculation
            Money calculatedSubtotal = item.calculateSubtotal();
            if (!item.getSubtotal().equals(calculatedSubtotal)) {
                throw new IllegalArgumentException(
                        "Item subtotal mismatch. Expected: " + calculatedSubtotal +
                                ", found: " + item.getSubtotal()
                );
            }

        });
    }

    /**
     * Validates that the current status can transition to the target status.
     *
     * @param nextStatus the target status
     * @throws IllegalStateException if the transition is invalid
     */
    private void validateTransition(OrderStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new IllegalStateException(
                    "Invalid status transition from " + this.status.value() +
                            " to " + nextStatus.value()
            );
        }
    }
}
