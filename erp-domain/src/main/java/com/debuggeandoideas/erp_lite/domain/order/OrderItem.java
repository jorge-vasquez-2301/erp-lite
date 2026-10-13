package com.debuggeandoideas.erp_lite.domain.order;

import com.debuggeandoideas.erp_lite.domain.common.Entity;
import com.debuggeandoideas.erp_lite.domain.product.Product;
import com.debuggeandoideas.erp_lite.domain.product.ProductId;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import com.debuggeandoideas.erp_lite.domain.shared.Quantity;
import lombok.Getter;

/**
 * OrderItem Entity.
 * Represents a line item in an order with product reference, quantity, and pricing.
 * Product name and unit price are snapshots taken at order creation time.
 */
@Getter
public class OrderItem extends Entity<OrderItemId> {

    private ProductId productReference;
    private String productName;
    private Quantity quantity;
    private Money unitPrice;
    private Money subtotal;

    protected OrderItem() {
        super(null);
    }

    /**
     * Creates an OrderItem from a Product and quantity.
     * This is a snapshot: product name and price are frozen at order creation.
     *
     * @param product  the product to order
     * @param quantity the quantity to order
     * @return a new OrderItem instance
     * @throws IllegalArgumentException if product is inactive or has insufficient stock
     */
    public static OrderItem from(Product product, Quantity quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (!product.isActive()) {
            throw new IllegalArgumentException("Cannot create order item for inactive product: " + product.getSku().value());
        }
        if (!product.hasAvailableStock(quantity.value())) {
            throw new IllegalArgumentException(
                    "Insufficient stock for product " + product.getSku().value() +
                            ". Required: " + quantity.value() + ", Available: " + product.getStock().value()
            );
        }

        OrderItemId orderItemId = OrderItemId.generate();
        Money unitPrice = product.getPrice();
        Money subtotal = unitPrice.multiply(quantity);

        return new OrderItem(
                orderItemId,
                product.getId(),
                product.getName().value(),
                quantity,
                unitPrice,
                subtotal
        );
    }

    private OrderItem(
            OrderItemId id,
            ProductId productReference,
            String productName,
            Quantity quantity,
            Money unitPrice,
            Money subtotal
    ) {
        super(id);
        this.productReference = productReference;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    /**
     * Calculates the subtotal (quantity * unitPrice).
     * This method exists for validation purposes.
     *
     * @return the calculated subtotal
     */
    public Money calculateSubtotal() {
        return this.unitPrice.multiply(this.quantity);
    }
}
