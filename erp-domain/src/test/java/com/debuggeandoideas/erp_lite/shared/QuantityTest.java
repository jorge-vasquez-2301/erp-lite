package com.debuggeandoideas.erp_lite.shared;

import com.debuggeandoideas.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Quantity Domain Test")
class QuantityTest {

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Quantity Is Null")
    void shouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
        final String msgEx = "Quantity cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Quantity(null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Quantity Is Zero")
    void shouldThrowIllegalArgumentExceptionWhenQuantityIsZero() {
        final String msgEx = "Quantity must be greater than 0";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Quantity(0));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Quantity Is Negative")
    void shouldThrowIllegalArgumentExceptionWhenQuantityIsNegative() {
        final String msgEx = "Quantity must be greater than 0";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Quantity(-5));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Create Quantity With Valid Positive Value")
    void shouldCreateQuantityWithValidPositiveValue() {
        Quantity quantity = new Quantity(10);

        assertEquals(10, quantity.value());
    }

    @Test
    @DisplayName("Should Create Quantity Using of Method")
    void shouldCreateQuantityUsingOfMethod() {
        Quantity quantity = Quantity.of(5);

        assertEquals(5, quantity.value());
    }

    @Test
    @DisplayName("Should Create Quantity With Minimum Valid Value")
    void shouldCreateQuantityWithMinimumValidValue() {
        Quantity quantity = new Quantity(1);

        assertEquals(1, quantity.value());
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By Value")
    void shouldSupportEqualsAndHashCodeByValue() {
        Quantity quantity1 = new Quantity(10);
        Quantity quantity2 = new Quantity(10);

        assertEquals(quantity1, quantity2);
        assertEquals(quantity1.hashCode(), quantity2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When Values Differ")
    void shouldNotBeEqualWhenValuesDiffer() {
        Quantity quantity1 = new Quantity(10);
        Quantity quantity2 = new Quantity(20);

        assertNotEquals(quantity1, quantity2);
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        Quantity quantity = new Quantity(10);

        assertNotNull(quantity.toString());
        assertFalse(quantity.toString().isEmpty());
        assertTrue(quantity.toString().contains("10"));
    }
}
