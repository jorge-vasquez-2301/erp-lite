package com.debuggeandoideas.erp_lite.product;

import com.debuggeandoideas.erp_lite.domain.entities.product.*;
import com.debuggeandoideas.erp_lite.domain.entities.product.events.ProductCreated;
import com.debuggeandoideas.erp_lite.domain.entities.product.events.ProductDeactivated;
import com.debuggeandoideas.erp_lite.domain.entities.product.events.ProductUpdated;
import com.debuggeandoideas.erp_lite.domain.entities.product.events.StockChanged;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Domain Test")
class ProductRootTest {

    private static final Currency USD = Currency.getInstance("USD");

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Price Is Null")
    void shouldThrowIllegalArgumentExceptionWhenPriceIsNull() {
        final String msgEx = "Price cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> ProductRoot.create(
                        SKU.of("LAPTOP-001"),
                        ProductName.of("Laptop"),
                        "Description",
                        null,
                        Stock.of(10),
                        CategoryReference.of("cat-electronics"),
                        ProductImage.of("https://example.com/image.jpg"),
                        "test-user"
                ));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Price Is Zero Or Negative")
    void shouldThrowIllegalArgumentExceptionWhenPriceIsZeroOrNegative() {
        final String msgEx = "Price must be greater than 0";

        IllegalArgumentException targetExZero = assertThrows(IllegalArgumentException.class,
                () -> ProductRoot.create(
                        SKU.of("LAPTOP-001"),
                        ProductName.of("Laptop"),
                        "Description",
                        Money.of(0.0, USD),
                        Stock.of(10),
                        CategoryReference.of("cat-electronics"),
                        ProductImage.of("https://example.com/image.jpg"),
                        "test-user"
                ));

        assertEquals(msgEx, targetExZero.getMessage());
    }

    @Test
    @DisplayName("Should Create Product With Valid Data And Register ProductCreated Event")
    void shouldCreateProductWithValidDataAndRegisterProductCreatedEvent() {
        SKU sku = SKU.of("LAPTOP-001");
        ProductName name = ProductName.of("Laptop Computer");
        String description = "High-performance laptop";
        Money price = Money.of(999.99, USD);
        Stock stock = Stock.of(100);
        CategoryReference category = CategoryReference.of("cat-electronics");
        ProductImage image = ProductImage.of("https://example.com/laptop.jpg");

        ProductRoot productRoot = ProductRoot.create(sku, name, description, price, stock, category, image, "test-user");

        assertNotNull(productRoot.getId());
        assertEquals(sku, productRoot.getSku());
        assertEquals(name, productRoot.getName());
        assertEquals(description, productRoot.getDescription());
        assertEquals(price, productRoot.getPrice());
        assertEquals(stock, productRoot.getStock());
        assertEquals(category, productRoot.getCategory());
        assertEquals(image, productRoot.getImage());
        assertTrue(productRoot.isActive());
        assertNotNull(productRoot.getAuditInfo());

        // Verify event registration
        assertEquals(1, productRoot.getDomainEvents().size());
        assertTrue(productRoot.getDomainEvents().get(0) instanceof ProductCreated);
    }

    @Test
    @DisplayName("Should Update Product Information And Register ProductUpdated Event")
    void shouldUpdateProductInformationAndRegisterProductUpdatedEvent() {
        ProductRoot productRoot = createValidProduct();
        productRoot.clearDomainEvents();

        ProductName newName = ProductName.of("Updated Laptop");
        String newDescription = "Updated description";
        Money newPrice = Money.of(1099.99, USD);
        CategoryReference newCategory = CategoryReference.of("cat-computers");
        ProductImage newImage = ProductImage.of("https://example.com/new-laptop.jpg");

        productRoot.update(newName, newDescription, newPrice, newCategory, newImage);

        assertEquals(newName, productRoot.getName());
        assertEquals(newDescription, productRoot.getDescription());
        assertEquals(newPrice, productRoot.getPrice());
        assertEquals(newCategory, productRoot.getCategory());
        assertEquals(newImage, productRoot.getImage());

        // Verify event registration
        assertEquals(1, productRoot.getDomainEvents().size());
        assertTrue(productRoot.getDomainEvents().get(0) instanceof ProductUpdated);
    }

    @Test
    @DisplayName("Should Increment Stock And Register StockChanged Event")
    void shouldIncrementStockAndRegisterStockChangedEvent() {
        ProductRoot productRoot = createValidProduct();
        productRoot.clearDomainEvents();

        int initialStock = productRoot.getStock().value();
        String reason = "Restocking from supplier";

        productRoot.incrementStock(50, reason);

        assertEquals(initialStock + 50, productRoot.getStock().value());

        // Verify event registration
        assertEquals(1, productRoot.getDomainEvents().size());
        StockChanged event = (StockChanged) productRoot.getDomainEvents().get(0);
        assertEquals(initialStock, event.oldStock());
        assertEquals(initialStock + 50, event.newStock());
        assertEquals(reason, event.reason());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Increment Reason Is Null Or Blank")
    void shouldThrowIllegalArgumentExceptionWhenIncrementReasonIsNullOrBlank() {
        ProductRoot productRoot = createValidProduct();

        IllegalArgumentException targetExNull = assertThrows(IllegalArgumentException.class,
                () -> productRoot.incrementStock(10, null));

        assertEquals("Reason for stock increment cannot be null or blank", targetExNull.getMessage());

        IllegalArgumentException targetExBlank = assertThrows(IllegalArgumentException.class,
                () -> productRoot.incrementStock(10, "   "));

        assertEquals("Reason for stock increment cannot be null or blank", targetExBlank.getMessage());
    }

    @Test
    @DisplayName("Should Decrement Stock And Register StockChanged Event")
    void shouldDecrementStockAndRegisterStockChangedEvent() {
        ProductRoot productRoot = createValidProduct();
        productRoot.clearDomainEvents();

        int initialStock = productRoot.getStock().value();
        String reason = "Sold items";

        productRoot.decrementStock(20, reason);

        assertEquals(initialStock - 20, productRoot.getStock().value());

        // Verify event registration
        assertEquals(1, productRoot.getDomainEvents().size());
        StockChanged event = (StockChanged) productRoot.getDomainEvents().get(0);
        assertEquals(initialStock, event.oldStock());
        assertEquals(initialStock - 20, event.newStock());
        assertEquals(reason, event.reason());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Decrement Reason Is Null Or Blank")
    void shouldThrowIllegalArgumentExceptionWhenDecrementReasonIsNullOrBlank() {
        ProductRoot productRoot = createValidProduct();

        IllegalArgumentException targetExNull = assertThrows(IllegalArgumentException.class,
                () -> productRoot.decrementStock(10, null));

        assertEquals("Reason for stock decrement cannot be null or blank", targetExNull.getMessage());

        IllegalArgumentException targetExBlank = assertThrows(IllegalArgumentException.class,
                () -> productRoot.decrementStock(10, ""));

        assertEquals("Reason for stock decrement cannot be null or blank", targetExBlank.getMessage());
    }

    @Test
    @DisplayName("Should Change Price And Register ProductUpdated Event")
    void shouldChangePriceAndRegisterProductUpdatedEvent() {
        ProductRoot productRoot = createValidProduct();
        productRoot.clearDomainEvents();

        Money newPrice = Money.of(1199.99, USD);

        productRoot.changePrice(newPrice);

        assertEquals(newPrice, productRoot.getPrice());

        // Verify event registration
        assertEquals(1, productRoot.getDomainEvents().size());
        assertTrue(productRoot.getDomainEvents().get(0) instanceof ProductUpdated);
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Changing To Null Or Invalid Price")
    void shouldThrowIllegalArgumentExceptionWhenChangingToNullOrInvalidPrice() {
        ProductRoot productRoot = createValidProduct();

        IllegalArgumentException targetExNull = assertThrows(IllegalArgumentException.class,
                () -> productRoot.changePrice(null));

        assertEquals("Price cannot be null", targetExNull.getMessage());

        IllegalArgumentException targetExZero = assertThrows(IllegalArgumentException.class,
                () -> productRoot.changePrice(Money.of(0.0, USD)));

        assertEquals("Price must be greater than 0", targetExZero.getMessage());
    }

    @Test
    @DisplayName("Should Deactivate Product And Register ProductDeactivated Event")
    void shouldDeactivateProductAndRegisterProductDeactivatedEvent() {
        ProductRoot productRoot = createValidProduct();
        productRoot.clearDomainEvents();

        assertTrue(productRoot.isActive());

        productRoot.deactivate();

        assertFalse(productRoot.isActive());

        // Verify event registration
        assertEquals(1, productRoot.getDomainEvents().size());
        assertTrue(productRoot.getDomainEvents().get(0) instanceof ProductDeactivated);
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Deactivating Already Deactivated Product")
    void shouldThrowIllegalStateExceptionWhenDeactivatingAlreadyDeactivatedProduct() {
        ProductRoot productRoot = createValidProduct();
        productRoot.deactivate();

        IllegalStateException targetEx = assertThrows(IllegalStateException.class,
                () -> productRoot.deactivate());

        assertEquals("Product is already deactivated", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Activate Product And Register ProductUpdated Event")
    void shouldActivateProductAndRegisterProductUpdatedEvent() {
        ProductRoot productRoot = createValidProduct();
        productRoot.deactivate();
        productRoot.clearDomainEvents();

        assertFalse(productRoot.isActive());

        productRoot.activate();

        assertTrue(productRoot.isActive());

        // Verify event registration
        assertEquals(1, productRoot.getDomainEvents().size());
        assertTrue(productRoot.getDomainEvents().get(0) instanceof ProductUpdated);
    }

    @Test
    @DisplayName("Should Throw IllegalStateException When Activating Already Active Product")
    void shouldThrowIllegalStateExceptionWhenActivatingAlreadyActiveProduct() {
        ProductRoot productRoot = createValidProduct();

        IllegalStateException targetEx = assertThrows(IllegalStateException.class,
                () -> productRoot.activate());

        assertEquals("Product is already active", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Return True When Product Has Available Stock And Is Active")
    void shouldReturnTrueWhenProductHasAvailableStockAndIsActive() {
        ProductRoot productRoot = createProductWithStock(100);

        assertTrue(productRoot.hasAvailableStock(50));
        assertTrue(productRoot.hasAvailableStock(100));
        assertTrue(productRoot.hasAvailableStock(1));
    }

    @Test
    @DisplayName("Should Return False When Product Does Not Have Available Stock")
    void shouldReturnFalseWhenProductDoesNotHaveAvailableStock() {
        ProductRoot productRoot = createProductWithStock(50);

        assertFalse(productRoot.hasAvailableStock(51));
        assertFalse(productRoot.hasAvailableStock(100));
    }

    @Test
    @DisplayName("Should Return False When Product Is Inactive Even With Stock")
    void shouldReturnFalseWhenProductIsInactiveEvenWithStock() {
        ProductRoot productRoot = createProductWithStock(100);
        productRoot.deactivate();

        assertFalse(productRoot.hasAvailableStock(10));
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By ID")
    void shouldSupportEqualsAndHashCodeByID() {
        ProductRoot productRoot1 = createValidProduct();
        ProductRoot productRoot2 = createValidProduct();

        // Different products should not be equal
        assertNotEquals(productRoot1, productRoot2);
        assertNotEquals(productRoot1.getId(), productRoot2.getId());
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        ProductRoot productRoot = createValidProduct();

        assertNotNull(productRoot.toString());
        assertFalse(productRoot.toString().isEmpty());
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
