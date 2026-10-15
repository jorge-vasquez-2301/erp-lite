package com.debuggeandoideas.erp_lite.order;

import com.debuggeandoideas.erp_lite.domain.entities.order.Customer;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderItem;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderNumber;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderRoot;
import com.debuggeandoideas.erp_lite.domain.entities.order.events.*;
import com.debuggeandoideas.erp_lite.domain.entities.product.*;
import com.debuggeandoideas.erp_lite.domain.shared.CustomerId;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import com.debuggeandoideas.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order Domain Test")
class OrderRootTest {

    private static final Currency USD = Currency.getInstance("USD");

    @Test
    @DisplayName("Should Throw IllegalArgumentException When OrderNumber Is Null")
    void shouldThrowIllegalArgumentExceptionWhenOrderNumberIsNull() {
        final String msgEx = "Order number cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> OrderRoot.create(
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
                () -> OrderRoot.create(
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
                () -> OrderRoot.create(
                        OrderNumber.of("ORD-2025-001"),
                        createCustomer(),
                        null,
                        "test-user"
                ));

        assertEquals(msgEx, targetExNull.getMessage());

        IllegalArgumentException targetExEmpty = assertThrows(IllegalArgumentException.class,
                () -> OrderRoot.create(
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

        OrderRoot orderRoot = OrderRoot.create(orderNumber, customer, items, "test-user");

        assertNotNull(orderRoot.getId());
        assertEquals(orderNumber, orderRoot.getOrderNumber());
        assertEquals(customer, orderRoot.getCustomer());
        assertTrue(orderRoot.getStatus().isPending());
        assertEquals(items.size(), orderRoot.getItems().size());
        assertNotNull(orderRoot.getTotalAmount());
        assertNotNull(orderRoot.getAuditInfo());

        // Verify event registration
        assertEquals(1, orderRoot.getDomainEvents().size());
        assertTrue(orderRoot.getDomainEvents().get(0) instanceof OrderCreated);
    }

    @Test
    @DisplayName("Should Calculate Total Amount Correctly")
    void shouldCalculateTotalAmountCorrectly() {
        List<OrderItem> items = createOrderItems();
        OrderRoot orderRoot = createValidOrder();

        Money expectedTotal = items.get(0).getSubtotal();
        for (int i = 1; i < items.size(); i++) {
            expectedTotal = expectedTotal.add(items.get(i).getSubtotal());
        }

        assertEquals(expectedTotal, orderRoot.getTotalAmount());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Items Have Different Currencies")
    void shouldThrowIllegalArgumentExceptionWhenItemsHaveDifferentCurrencies() {
        ProductRoot productRoot1 = createProduct("LAPTOP-001", "Laptop", 999.99, USD);
        ProductRoot productRoot2 = createProduct("MOUSE-001", "Mouse", 29.99, Currency.getInstance("EUR"));

        OrderItem item1 = OrderItem.from(productRoot1, Quantity.of(1));
        OrderItem item2 = OrderItem.from(productRoot2, Quantity.of(1));

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> OrderRoot.create(
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
        OrderRoot orderRoot = createValidOrder();
        orderRoot.clearDomainEvents();

        assertTrue(orderRoot.getStatus().isPending());

        orderRoot.confirm();

        assertTrue(orderRoot.getStatus().isConfirmed());

        // Verify event registration
        assertEquals(1, orderRoot.getDomainEvents().size());
        assertTrue(orderRoot.getDomainEvents().get(0) instanceof OrderConfirmed);
    }

    @Test
    @DisplayName("Should Ship Order And Register OrderShipped Event")
    void shouldShipOrderAndRegisterOrderShippedEvent() {
        OrderRoot orderRoot = createValidOrder();
        orderRoot.confirm();
        orderRoot.clearDomainEvents();

        assertTrue(orderRoot.getStatus().isConfirmed());

        orderRoot.ship();

        assertTrue(orderRoot.getStatus().isShipped());

        // Verify event registration
        assertEquals(1, orderRoot.getDomainEvents().size());
        assertTrue(orderRoot.getDomainEvents().get(0) instanceof OrderShipped);
    }

    @Test
    @DisplayName("Should Deliver Order And Register OrderDelivered Event")
    void shouldDeliverOrderAndRegisterOrderDeliveredEvent() {
        OrderRoot orderRoot = createValidOrder();
        orderRoot.confirm();
        orderRoot.ship();
        orderRoot.clearDomainEvents();

        assertTrue(orderRoot.getStatus().isShipped());

        orderRoot.deliver();

        assertTrue(orderRoot.getStatus().isDelivered());

        // Verify event registration
        assertEquals(1, orderRoot.getDomainEvents().size());
        assertTrue(orderRoot.getDomainEvents().get(0) instanceof OrderDelivered);
    }

    @Test
    @DisplayName("Should Cancel Order And Register OrderCancelled Event")
    void shouldCancelOrderAndRegisterOrderCancelledEvent() {
        OrderRoot orderRoot = createValidOrder();
        orderRoot.clearDomainEvents();

        String reason = "Customer requested cancellation";

        orderRoot.cancel(reason);

        assertTrue(orderRoot.getStatus().isCancelled());

        // Verify event registration
        assertEquals(1, orderRoot.getDomainEvents().size());
        OrderCancelled event = (OrderCancelled) orderRoot.getDomainEvents().get(0);
        assertEquals(reason, event.reason());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Cancellation Reason Is Null Or Blank")
    void shouldThrowIllegalArgumentExceptionWhenCancellationReasonIsNullOrBlank() {
        OrderRoot orderRoot = createValidOrder();

        IllegalArgumentException targetExNull = assertThrows(IllegalArgumentException.class,
                () -> orderRoot.cancel(null));

        assertEquals("Cancellation reason cannot be null or blank", targetExNull.getMessage());

        IllegalArgumentException targetExBlank = assertThrows(IllegalArgumentException.class,
                () -> orderRoot.cancel("   "));

        assertEquals("Cancellation reason cannot be null or blank", targetExBlank.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Invalid Status Transition")
    void shouldThrowIllegalStateExceptionWhenInvalidStatusTransition() {
        OrderRoot orderRoot = createValidOrder();

        // Cannot ship a PENDING order
        IllegalStateException targetEx1 = assertThrows(IllegalStateException.class,
                () -> orderRoot.ship());

        assertTrue(targetEx1.getMessage().contains("Invalid status transition"));

        // Cannot deliver a PENDING order
        IllegalStateException targetEx2 = assertThrows(IllegalStateException.class,
                () -> orderRoot.deliver());

        assertTrue(targetEx2.getMessage().contains("Invalid status transition"));
    }

    @Test
    @DisplayName("Should Add Item To Order When Status Is PENDING")
    void shouldAddItemToOrderWhenStatusIsPENDING() {
        OrderRoot orderRoot = createValidOrder();
        int initialItemCount = orderRoot.getItems().size();
        Money initialTotal = orderRoot.getTotalAmount();

        ProductRoot newProductRoot = createProduct("KEYBOARD-001", "Keyboard", 79.99, USD);
        OrderItem newItem = OrderItem.from(newProductRoot, Quantity.of(1));

        orderRoot.addItem(newItem);

        assertEquals(initialItemCount + 1, orderRoot.getItems().size());
        assertTrue(orderRoot.getTotalAmount().amount().compareTo(initialTotal.amount()) > 0);
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Adding Item To Non-PENDING Order")
    void shouldThrowIllegalStateExceptionWhenAddingItemToNonPENDINGOrder() {
        OrderRoot orderRoot = createValidOrder();
        orderRoot.confirm();

        ProductRoot newProductRoot = createProduct("KEYBOARD-001", "Keyboard", 79.99, USD);
        OrderItem newItem = OrderItem.from(newProductRoot, Quantity.of(1));

        IllegalStateException targetEx = assertThrows(IllegalStateException.class,
                () -> orderRoot.addItem(newItem));

        assertTrue(targetEx.getMessage().contains("Cannot add items to order in status"));
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Adding Null Item")
    void shouldThrowIllegalArgumentExceptionWhenAddingNullItem() {
        OrderRoot orderRoot = createValidOrder();

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> orderRoot.addItem(null));

        assertEquals("Order item cannot be null", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Remove Item From Order When Status Is PENDING")
    void shouldRemoveItemFromOrderWhenStatusIsPENDING() {
        OrderRoot orderRoot = createValidOrder();
        OrderItem itemToRemove = orderRoot.getItems().get(0);
        int initialItemCount = orderRoot.getItems().size();
        Money initialTotal = orderRoot.getTotalAmount();

        orderRoot.removeItem(itemToRemove);

        assertEquals(initialItemCount - 1, orderRoot.getItems().size());
        assertTrue(orderRoot.getTotalAmount().amount().compareTo(initialTotal.amount()) < 0);
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Removing Item From Non-PENDING Order")
    void shouldThrowIllegalStateExceptionWhenRemovingItemFromNonPENDINGOrder() {
        OrderRoot orderRoot = createValidOrder();
        orderRoot.confirm();
        OrderItem itemToRemove = orderRoot.getItems().get(0);

        IllegalStateException targetEx = assertThrows(IllegalStateException.class,
                () -> orderRoot.removeItem(itemToRemove));

        assertTrue(targetEx.getMessage().contains("Cannot remove items from order in status"));
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Removing Null Item")
    void shouldThrowIllegalArgumentExceptionWhenRemovingNullItem() {
        OrderRoot orderRoot = createValidOrder();

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> orderRoot.removeItem(null));

        assertEquals("Order item cannot be null", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Removing Non-Existent Item")
    void shouldThrowIllegalArgumentExceptionWhenRemovingNonExistentItem() {
        OrderRoot orderRoot = createValidOrder();
        ProductRoot otherProductRoot = createProduct("OTHER-001", "Other", 50.0, USD);
        OrderItem nonExistentItem = OrderItem.from(otherProductRoot, Quantity.of(1));

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> orderRoot.removeItem(nonExistentItem));

        assertEquals("Item not found in order", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Removing Last Item")
    void shouldThrowIllegalStateExceptionWhenRemovingLastItem() {
        ProductRoot productRoot = createProduct("LAPTOP-001", "Laptop", 999.99, USD);
        OrderItem item = OrderItem.from(productRoot, Quantity.of(1));
        OrderRoot orderRoot = OrderRoot.create(
                OrderNumber.of("ORD-2025-001"),
                createCustomer(),
                List.of(item),
                "test-user"
        );

        IllegalStateException targetEx = assertThrows(IllegalStateException.class,
                () -> orderRoot.removeItem(item));

        assertEquals("Order must have at least one item", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Return Unmodifiable List Of Items")
    void shouldReturnUnmodifiableListOfItems() {
        OrderRoot orderRoot = createValidOrder();
        List<OrderItem> items = orderRoot.getItems();

        ProductRoot newProductRoot = createProduct("KEYBOARD-001", "Keyboard", 79.99, USD);
        OrderItem newItem = OrderItem.from(newProductRoot, Quantity.of(1));

        assertThrows(UnsupportedOperationException.class,
                () -> items.add(newItem));
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By ID")
    void shouldSupportEqualsAndHashCodeByID() {
        OrderRoot orderRoot1 = createValidOrder();
        OrderRoot orderRoot2 = createValidOrder();

        // Different orders should not be equal
        assertNotEquals(orderRoot1, orderRoot2);
        assertNotEquals(orderRoot1.getId(), orderRoot2.getId());
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        OrderRoot orderRoot = createValidOrder();

        assertNotNull(orderRoot.toString());
        assertFalse(orderRoot.toString().isEmpty());
    }

    private OrderRoot createValidOrder() {
        return OrderRoot.create(
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
        ProductRoot productRoot1 = createProduct("LAPTOP-001", "Laptop Computer", 999.99, USD);
        ProductRoot productRoot2 = createProduct("MOUSE-001", "Wireless Mouse", 29.99, USD);

        OrderItem item1 = OrderItem.from(productRoot1, Quantity.of(1));
        OrderItem item2 = OrderItem.from(productRoot2, Quantity.of(2));

        return List.of(item1, item2);
    }

    private ProductRoot createProduct(String skuValue, String name, double price, Currency currency) {
        return ProductRoot.create(
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
