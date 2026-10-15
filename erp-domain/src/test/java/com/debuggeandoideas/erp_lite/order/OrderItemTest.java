package com.debuggeandoideas.erp_lite.order;

import com.debuggeandoideas.erp_lite.domain.entities.order.OrderItem;
import com.debuggeandoideas.erp_lite.domain.entities.product.*;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import com.debuggeandoideas.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItem Domain Test")
class OrderItemTest {

    private static final Currency USD = Currency.getInstance("USD");

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Product Is Null")
    void shouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        final String msgEx = "Product cannot be null";
        Quantity quantity = Quantity.of(5);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> OrderItem.from(null, quantity));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Quantity Is Null")
    void shouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
        final String msgEx = "Quantity cannot be null";
        ProductRoot productRoot = createValidProduct();

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> OrderItem.from(productRoot, null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Product Is Inactive")
    void shouldThrowIllegalArgumentExceptionWhenProductIsInactive() {
        ProductRoot productRoot = createValidProduct();
        productRoot.deactivate();
        Quantity quantity = Quantity.of(5);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> OrderItem.from(productRoot, quantity));

        assertTrue(targetEx.getMessage().contains("Cannot create order item for inactive product"));
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Product Has Insufficient Stock")
    void shouldThrowIllegalArgumentExceptionWhenProductHasInsufficientStock() {
        ProductRoot productRoot = createProductWithStock(5);
        Quantity quantity = Quantity.of(10);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> OrderItem.from(productRoot, quantity));

        assertTrue(targetEx.getMessage().contains("Insufficient stock for product"));
        assertTrue(targetEx.getMessage().contains("Required: 10"));
        assertTrue(targetEx.getMessage().contains("Available: 5"));
    }

    @Test
    @DisplayName("Should Create OrderItem With Valid Product And Quantity")
    void shouldCreateOrderItemWithValidProductAndQuantity() {
        ProductRoot productRoot = createValidProduct();
        Quantity quantity = Quantity.of(5);

        OrderItem orderItem = OrderItem.from(productRoot, quantity);

        assertNotNull(orderItem.getId());
        assertEquals(productRoot.getId(), orderItem.getProductReference());
        assertEquals(productRoot.getName().value(), orderItem.getProductName());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(productRoot.getPrice(), orderItem.getUnitPrice());
        assertEquals(productRoot.getPrice().multiply(quantity), orderItem.getSubtotal());
    }

    @Test
    @DisplayName("Should Capture Product Name And Price As Snapshot")
    void shouldCaptureProductNameAndPriceAsSnapshot() {
        ProductRoot productRoot = createValidProduct();
        Quantity quantity = Quantity.of(3);
        Money originalPrice = productRoot.getPrice();
        String originalName = productRoot.getName().value();

        OrderItem orderItem = OrderItem.from(productRoot, quantity);

        // Modify product after order item creation
        productRoot.changePrice(Money.of(999.99, USD));
        productRoot.update(
                ProductName.of("Modified Product Name"),
                "New Description",
                productRoot.getPrice(),
                productRoot.getCategory(),
                productRoot.getImage()
        );

        // OrderItem should preserve original values
        assertEquals(originalName, orderItem.getProductName());
        assertEquals(originalPrice, orderItem.getUnitPrice());
        assertEquals(originalPrice.multiply(quantity), orderItem.getSubtotal());
    }

    @Test
    @DisplayName("Should Calculate Subtotal Correctly")
    void shouldCalculateSubtotalCorrectly() {
        ProductRoot productRoot = createValidProduct();
        Quantity quantity = Quantity.of(4);

        OrderItem orderItem = OrderItem.from(productRoot, quantity);

        Money expectedSubtotal = productRoot.getPrice().multiply(quantity);
        assertEquals(expectedSubtotal, orderItem.calculateSubtotal());
        assertEquals(expectedSubtotal, orderItem.getSubtotal());
    }

    @Test
    @DisplayName("Should Create OrderItem With Exact Stock Available")
    void shouldCreateOrderItemWithExactStockAvailable() {
        ProductRoot productRoot = createProductWithStock(10);
        Quantity quantity = Quantity.of(10);

        OrderItem orderItem = assertDoesNotThrow(() -> OrderItem.from(productRoot, quantity));

        assertEquals(quantity, orderItem.getQuantity());
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By ID")
    void shouldSupportEqualsAndHashCodeByID() {
        ProductRoot productRoot = createValidProduct();
        Quantity quantity = Quantity.of(5);

        OrderItem orderItem1 = OrderItem.from(productRoot, quantity);
        OrderItem orderItem2 = OrderItem.from(productRoot, quantity);

        // Different instances with different IDs should not be equal
        assertNotEquals(orderItem1, orderItem2);
        assertNotEquals(orderItem1.getId(), orderItem2.getId());
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        ProductRoot productRoot = createValidProduct();
        Quantity quantity = Quantity.of(5);

        OrderItem orderItem = OrderItem.from(productRoot, quantity);

        assertNotNull(orderItem.toString());
        assertFalse(orderItem.toString().isEmpty());
    }

    private ProductRoot createValidProduct() {
        return ProductRoot.create(
                SKU.of("LAPTOP-001"),
                ProductName.of("Laptop Computer"),
                "High-performance laptop",
                Money.of(999.99, USD),
                Stock.of(100),
                CategoryReference.of("cat-electronics"),
                ProductImage.of("https://example.com/laptop.jpg"),
                "test-user"
        );
    }

    private ProductRoot createProductWithStock(int stockAmount) {
        return ProductRoot.create(
                SKU.of("MOUSE-001"),
                ProductName.of("Wireless Mouse"),
                "Ergonomic wireless mouse",
                Money.of(29.99, USD),
                Stock.of(stockAmount),
                CategoryReference.of("cat-electronics"),
                ProductImage.of("https://example.com/mouse.jpg"),
                "test-user"
        );
    }
}
