package com.debuggeandoideas.erp_lite.order;

import com.debuggeandoideas.erp_lite.domain.order.*;
import com.debuggeandoideas.erp_lite.domain.order.events.*;
import com.debuggeandoideas.erp_lite.domain.product.*;
import com.debuggeandoideas.erp_lite.domain.shared.CustomerId;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import com.debuggeandoideas.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order Domain Test")
class OrderTest {

    private static final Currency USD = Currency.getInstance("USD");

    @Test
    @DisplayName("Should Throw IllegalArgumentException When OrderNumber Is Null")
    void shouldThrowIllegalArgumentExceptionWhenOrderNumberIsNull() {
        final String msgEx = "Order number cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> Order.create(
                        null,
                        createCustomer(),
                        createOrderItems(),
                        "test-user"
                ));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Customer Is Null")
    void shouldThrowIllegalArgumentExceptionWhenCustomerIsNull() {
        final String msgEx = "Customer cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> Order.create(
                        OrderNumber.of("ORD-2025-001"),
                        null,
                        createOrderItems(),
                        "test-user"
                ));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Items List Is Null Or Empty")
    void shouldThrowIllegalArgumentExceptionWhenItemsListIsNullOrEmpty() {
        final String msgEx = "Order must have at least one item";

        IllegalArgumentException targetExNull = assertThrows(IllegalArgumentException.class,
                () -> Order.create(
                        OrderNumber.of("ORD-2025-001"),
                        createCustomer(),
                        null,
                        "test-user"
                ));

        assertEquals(msgEx, targetExNull.getMessage());

        IllegalArgumentException targetExEmpty = assertThrows(IllegalArgumentException.class,
                () -> Order.create(
                        OrderNumber.of("ORD-2025-001"),
                        createCustomer(),
                        List.of(),
                        "test-user"
                ));

        assertEquals(msgEx, targetExEmpty.getMessage());
    }

    @Test
    @DisplayName("Should Create Order With Valid Data And Register OrderCreated Event")
    void shouldCreateOrderWithValidDataAndRegisterOrderCreatedEvent() {
        OrderNumber orderNumber = OrderNumber.of("ORD-2025-001");
        Customer customer = createCustomer();
        List<OrderItem> items = createOrderItems();

        Order order = Order.create(orderNumber, customer, items, "test-user");

        assertNotNull(order.getId());
        assertEquals(orderNumber, order.getOrderNumber());
        assertEquals(customer, order.getCustomer());
        assertTrue(order.getStatus().isPending());
        assertEquals(items.size(), order.getItems().size());
        assertNotNull(order.getTotalAmount());
        assertNotNull(order.getAuditInfo());

        // Verify event registration
        assertEquals(1, order.getDomainEvents().size());
        assertTrue(order.getDomainEvents().get(0) instanceof OrderCreated);
    }

    @Test
    @DisplayName("Should Calculate Total Amount Correctly")
    void shouldCalculateTotalAmountCorrectly() {
        List<OrderItem> items = createOrderItems();
        Order order = createValidOrder();

        Money expectedTotal = items.get(0).getSubtotal();
        for (int i = 1; i < items.size(); i++) {
            expectedTotal = expectedTotal.add(items.get(i).getSubtotal());
        }

        assertEquals(expectedTotal, order.getTotalAmount());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Items Have Different Currencies")
    void shouldThrowIllegalArgumentExceptionWhenItemsHaveDifferentCurrencies() {
        Product product1 = createProduct("LAPTOP-001", "Laptop", 999.99, USD);
        Product product2 = createProduct("MOUSE-001", "Mouse", 29.99, Currency.getInstance("EUR"));

        OrderItem item1 = OrderItem.from(product1, Quantity.of(1));
        OrderItem item2 = OrderItem.from(product2, Quantity.of(1));

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> Order.create(
                        OrderNumber.of("ORD-2025-001"),
                        createCustomer(),
                        List.of(item1, item2),
                        "test-user"
                ));

        assertTrue(targetEx.getMessage().contains("All items must have the same currency"));
    }

    @Test
    @DisplayName("Should Confirm Order And Register OrderConfirmed Event")
    void shouldConfirmOrderAndRegisterOrderConfirmedEvent() {
        Order order = createValidOrder();
        order.clearDomainEvents();

        assertTrue(order.getStatus().isPending());

        order.confirm();

        assertTrue(order.getStatus().isConfirmed());

        // Verify event registration
        assertEquals(1, order.getDomainEvents().size());
        assertTrue(order.getDomainEvents().get(0) instanceof OrderConfirmed);
    }

    @Test
    @DisplayName("Should Ship Order And Register OrderShipped Event")
    void shouldShipOrderAndRegisterOrderShippedEvent() {
        Order order = createValidOrder();
        order.confirm();
        order.clearDomainEvents();

        assertTrue(order.getStatus().isConfirmed());

        order.ship();

        assertTrue(order.getStatus().isShipped());

        // Verify event registration
        assertEquals(1, order.getDomainEvents().size());
        assertTrue(order.getDomainEvents().get(0) instanceof OrderShipped);
    }

    @Test
    @DisplayName("Should Deliver Order And Register OrderDelivered Event")
    void shouldDeliverOrderAndRegisterOrderDeliveredEvent() {
        Order order = createValidOrder();
        order.confirm();
        order.ship();
        order.clearDomainEvents();

        assertTrue(order.getStatus().isShipped());

        order.deliver();

        assertTrue(order.getStatus().isDelivered());

        // Verify event registration
        assertEquals(1, order.getDomainEvents().size());
        assertTrue(order.getDomainEvents().get(0) instanceof OrderDelivered);
    }

    @Test
    @DisplayName("Should Cancel Order And Register OrderCancelled Event")
    void shouldCancelOrderAndRegisterOrderCancelledEvent() {
        Order order = createValidOrder();
        order.clearDomainEvents();

        String reason = "Customer requested cancellation";

        order.cancel(reason);

        assertTrue(order.getStatus().isCancelled());

        // Verify event registration
        assertEquals(1, order.getDomainEvents().size());
        OrderCancelled event = (OrderCancelled) order.getDomainEvents().get(0);
        assertEquals(reason, event.reason());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Cancellation Reason Is Null Or Blank")
    void shouldThrowIllegalArgumentExceptionWhenCancellationReasonIsNullOrBlank() {
        Order order = createValidOrder();

        IllegalArgumentException targetExNull = assertThrows(IllegalArgumentException.class,
                () -> order.cancel(null));

        assertEquals("Cancellation reason cannot be null or blank", targetExNull.getMessage());

        IllegalArgumentException targetExBlank = assertThrows(IllegalArgumentException.class,
                () -> order.cancel("   "));

        assertEquals("Cancellation reason cannot be null or blank", targetExBlank.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Invalid Status Transition")
    void shouldThrowIllegalStateExceptionWhenInvalidStatusTransition() {
        Order order = createValidOrder();

        // Cannot ship a PENDING order
        IllegalStateException targetEx1 = assertThrows(IllegalStateException.class,
                () -> order.ship());

        assertTrue(targetEx1.getMessage().contains("Invalid status transition"));

        // Cannot deliver a PENDING order
        IllegalStateException targetEx2 = assertThrows(IllegalStateException.class,
                () -> order.deliver());

        assertTrue(targetEx2.getMessage().contains("Invalid status transition"));
    }

    @Test
    @DisplayName("Should Add Item To Order When Status Is PENDING")
    void shouldAddItemToOrderWhenStatusIsPENDING() {
        Order order = createValidOrder();
        int initialItemCount = order.getItems().size();
        Money initialTotal = order.getTotalAmount();

        Product newProduct = createProduct("KEYBOARD-001", "Keyboard", 79.99, USD);
        OrderItem newItem = OrderItem.from(newProduct, Quantity.of(1));

        order.addItem(newItem);

        assertEquals(initialItemCount + 1, order.getItems().size());
        assertTrue(order.getTotalAmount().amount().compareTo(initialTotal.amount()) > 0);
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Adding Item To Non-PENDING Order")
    void shouldThrowIllegalStateExceptionWhenAddingItemToNonPENDINGOrder() {
        Order order = createValidOrder();
        order.confirm();

        Product newProduct = createProduct("KEYBOARD-001", "Keyboard", 79.99, USD);
        OrderItem newItem = OrderItem.from(newProduct, Quantity.of(1));

        IllegalStateException targetEx = assertThrows(IllegalStateException.class,
                () -> order.addItem(newItem));

        assertTrue(targetEx.getMessage().contains("Cannot add items to order in status"));
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Adding Null Item")
    void shouldThrowIllegalArgumentExceptionWhenAddingNullItem() {
        Order order = createValidOrder();

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> order.addItem(null));

        assertEquals("Order item cannot be null", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Remove Item From Order When Status Is PENDING")
    void shouldRemoveItemFromOrderWhenStatusIsPENDING() {
        Order order = createValidOrder();
        OrderItem itemToRemove = order.getItems().get(0);
        int initialItemCount = order.getItems().size();
        Money initialTotal = order.getTotalAmount();

        order.removeItem(itemToRemove);

        assertEquals(initialItemCount - 1, order.getItems().size());
        assertTrue(order.getTotalAmount().amount().compareTo(initialTotal.amount()) < 0);
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Removing Item From Non-PENDING Order")
    void shouldThrowIllegalStateExceptionWhenRemovingItemFromNonPENDINGOrder() {
        Order order = createValidOrder();
        order.confirm();
        OrderItem itemToRemove = order.getItems().get(0);

        IllegalStateException targetEx = assertThrows(IllegalStateException.class,
                () -> order.removeItem(itemToRemove));

        assertTrue(targetEx.getMessage().contains("Cannot remove items from order in status"));
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Removing Null Item")
    void shouldThrowIllegalArgumentExceptionWhenRemovingNullItem() {
        Order order = createValidOrder();

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> order.removeItem(null));

        assertEquals("Order item cannot be null", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Removing Non-Existent Item")
    void shouldThrowIllegalArgumentExceptionWhenRemovingNonExistentItem() {
        Order order = createValidOrder();
        Product otherProduct = createProduct("OTHER-001", "Other", 50.0, USD);
        OrderItem nonExistentItem = OrderItem.from(otherProduct, Quantity.of(1));

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> order.removeItem(nonExistentItem));

        assertEquals("Item not found in order", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Removing Last Item")
    void shouldThrowIllegalStateExceptionWhenRemovingLastItem() {
        Product product = createProduct("LAPTOP-001", "Laptop", 999.99, USD);
        OrderItem item = OrderItem.from(product, Quantity.of(1));
        Order order = Order.create(
                OrderNumber.of("ORD-2025-001"),
                createCustomer(),
                List.of(item),
                "test-user"
        );

        IllegalStateException targetEx = assertThrows(IllegalStateException.class,
                () -> order.removeItem(item));

        assertEquals("Order must have at least one item", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Return Unmodifiable List Of Items")
    void shouldReturnUnmodifiableListOfItems() {
        Order order = createValidOrder();
        List<OrderItem> items = order.getItems();

        Product newProduct = createProduct("KEYBOARD-001", "Keyboard", 79.99, USD);
        OrderItem newItem = OrderItem.from(newProduct, Quantity.of(1));

        assertThrows(UnsupportedOperationException.class,
                () -> items.add(newItem));
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By ID")
    void shouldSupportEqualsAndHashCodeByID() {
        Order order1 = createValidOrder();
        Order order2 = createValidOrder();

        // Different orders should not be equal
        assertNotEquals(order1, order2);
        assertNotEquals(order1.getId(), order2.getId());
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        Order order = createValidOrder();

        assertNotNull(order.toString());
        assertFalse(order.toString().isEmpty());
    }

    private Order createValidOrder() {
        return Order.create(
                OrderNumber.of("ORD-2025-001"),
                createCustomer(),
                createOrderItems(),
                "test-user"
        );
    }

    private Customer createCustomer() {
        return Customer.of(CustomerId.of(1L), "John Doe");
    }

    private List<OrderItem> createOrderItems() {
        Product product1 = createProduct("LAPTOP-001", "Laptop Computer", 999.99, USD);
        Product product2 = createProduct("MOUSE-001", "Wireless Mouse", 29.99, USD);

        OrderItem item1 = OrderItem.from(product1, Quantity.of(1));
        OrderItem item2 = OrderItem.from(product2, Quantity.of(2));

        return List.of(item1, item2);
    }

    private Product createProduct(String skuValue, String name, double price, Currency currency) {
        return Product.create(
                SKU.of(skuValue),
                ProductName.of(name),
                "Description for " + name,
                Money.of(price, currency),
                Stock.of(100),
                CategoryReference.of("cat-electronics"),
                ProductImage.of("https://example.com/image.jpg"),
                "test-user"
        );
    }
}
