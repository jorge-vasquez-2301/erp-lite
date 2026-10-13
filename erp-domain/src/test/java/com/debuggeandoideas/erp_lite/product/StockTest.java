package com.debuggeandoideas.erp_lite.product;

import com.debuggeandoideas.erp_lite.domain.product.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Stock Domain Test")
class StockTest {

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Stock Is Null")
    void shouldThrowIllegalArgumentExceptionWhenStockIsNull() {
        final String msgEx = "Stock cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Stock(null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Stock Is Negative")
    void shouldThrowIllegalArgumentExceptionWhenStockIsNegative() {
        final String msgEx = "Stock cannot be negative";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Stock(-1));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Create Stock With Valid Positive Value")
    void shouldCreateStockWithValidPositiveValue() {
        Stock stock = new Stock(100);

        assertEquals(100, stock.value());
    }

    @Test
    @DisplayName("Should Create Stock Using of Method")
    void shouldCreateStockUsingOfMethod() {
        Stock stock = Stock.of(50);

        assertEquals(50, stock.value());
    }

    @Test
    @DisplayName("Should Create Stock With Zero Value")
    void shouldCreateStockWithZeroValue() {
        Stock stock = new Stock(0);

        assertEquals(0, stock.value());
    }

    @Test
    @DisplayName("Should Create Zero Stock Using zero Method")
    void shouldCreateZeroStockUsingZeroMethod() {
        Stock stock = Stock.zero();

        assertEquals(0, stock.value());
    }

    @Test
    @DisplayName("Should Increment Stock By Valid Quantity")
    void shouldIncrementStockByValidQuantity() {
        Stock stock = Stock.of(10);

        Stock newStock = stock.increment(5);

        assertEquals(15, newStock.value());
        assertEquals(10, stock.value()); // Original should be unchanged (immutable)
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Increment Quantity Is Negative")
    void shouldThrowIllegalArgumentExceptionWhenIncrementQuantityIsNegative() {
        Stock stock = Stock.of(10);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> stock.increment(-5));

        assertEquals("Increment quantity cannot be negative", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Allow Increment By Zero")
    void shouldAllowIncrementByZero() {
        Stock stock = Stock.of(10);

        Stock newStock = stock.increment(0);

        assertEquals(10, newStock.value());
    }

    @Test
    @DisplayName("Should Decrement Stock By Valid Quantity")
    void shouldDecrementStockByValidQuantity() {
        Stock stock = Stock.of(10);

        Stock newStock = stock.decrement(5);

        assertEquals(5, newStock.value());
        assertEquals(10, stock.value()); // Original should be unchanged (immutable)
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Decrement Quantity Is Negative")
    void shouldThrowIllegalArgumentExceptionWhenDecrementQuantityIsNegative() {
        Stock stock = Stock.of(10);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> stock.decrement(-5));

        assertEquals("Decrement quantity cannot be negative", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Decrement Result Would Be Negative")
    void shouldThrowIllegalArgumentExceptionWhenDecrementResultWouldBeNegative() {
        Stock stock = Stock.of(10);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> stock.decrement(15));

        assertTrue(targetEx.getMessage().contains("Cannot decrement stock below zero"));
        assertTrue(targetEx.getMessage().contains("Current: 10"));
        assertTrue(targetEx.getMessage().contains("requested: 15"));
    }

    @Test
    @DisplayName("Should Allow Decrement To Zero")
    void shouldAllowDecrementToZero() {
        Stock stock = Stock.of(10);

        Stock newStock = stock.decrement(10);

        assertEquals(0, newStock.value());
    }

    @Test
    @DisplayName("Should Allow Decrement By Zero")
    void shouldAllowDecrementByZero() {
        Stock stock = Stock.of(10);

        Stock newStock = stock.decrement(0);

        assertEquals(10, newStock.value());
    }

    @Test
    @DisplayName("Should Return True When Stock Has Available Quantity")
    void shouldReturnTrueWhenStockHasAvailableQuantity() {
        Stock stock = Stock.of(10);

        assertTrue(stock.hasAvailable(10));
        assertTrue(stock.hasAvailable(5));
        assertTrue(stock.hasAvailable(0));
    }

    @Test
    @DisplayName("Should Return False When Stock Does Not Have Available Quantity")
    void shouldReturnFalseWhenStockDoesNotHaveAvailableQuantity() {
        Stock stock = Stock.of(10);

        assertFalse(stock.hasAvailable(11));
        assertFalse(stock.hasAvailable(100));
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By Value")
    void shouldSupportEqualsAndHashCodeByValue() {
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(10);

        assertEquals(stock1, stock2);
        assertEquals(stock1.hashCode(), stock2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When Values Differ")
    void shouldNotBeEqualWhenValuesDiffer() {
        Stock stock1 = new Stock(10);
        Stock stock2 = new Stock(20);

        assertNotEquals(stock1, stock2);
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        Stock stock = new Stock(10);

        assertNotNull(stock.toString());
        assertFalse(stock.toString().isEmpty());
        assertTrue(stock.toString().contains("10"));
    }
}
