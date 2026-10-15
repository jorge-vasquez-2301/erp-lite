package com.debuggeandoideas.erp_lite.product;

import com.debuggeandoideas.erp_lite.domain.entities.product.SKU;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SKU Domain Test")
class SKUTest {

    @Test
    @DisplayName("Should Throw IllegalArgumentException When SKU Is Null")
    void shouldThrowIllegalArgumentExceptionWhenSKUIsNull() {
        final String msgEx = "SKU cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new SKU(null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When SKU Format Is Invalid")
    void shouldThrowIllegalArgumentExceptionWhenSKUFormatIsInvalid() {
        String[] invalidSKUs = {
                "laptop-001",      // lowercase letters
                "LAPTOP001",       // missing hyphen
                "LAPTOP-01",       // only 2 digits
                "LAPTOP-0001",     // 4 digits
                "123-001",         // starts with numbers
                "LAP TOP-001",     // contains space
                "LAPTOP-ABC",      // letters instead of numbers
                "",                // empty
                "-001"             // missing prefix
        };

        for (String invalidSKU : invalidSKUs) {
            IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                    () -> new SKU(invalidSKU),
                    "Should throw exception for: " + invalidSKU);

            assertTrue(targetEx.getMessage().contains("Invalid SKU format"),
                    "Exception message should contain 'Invalid SKU format' for: " + invalidSKU);
        }
    }

    @Test
    @DisplayName("Should Create SKU With Valid Format")
    void shouldCreateSKUWithValidFormat() {
        String validSKU = "LAPTOP-001";

        SKU sku = new SKU(validSKU);

        assertEquals(validSKU, sku.value());
    }

    @Test
    @DisplayName("Should Create SKU Using of Method")
    void shouldCreateSKUUsingOfMethod() {
        String validSKU = "MOUSE-042";

        SKU sku = SKU.of(validSKU);

        assertEquals(validSKU, sku.value());
    }

    @Test
    @DisplayName("Should Accept Valid SKU Formats")
    void shouldAcceptValidSKUFormats() {
        String[] validSKUs = {
                "LAPTOP-001",
                "MOUSE-042",
                "KEYBOARD-999",
                "AB-123",
                "A-001",
                "MONITOR-000",
                "HEADPHONES-555"
        };

        for (String validSKU : validSKUs) {
            SKU sku = assertDoesNotThrow(() -> new SKU(validSKU),
                    "Should not throw exception for: " + validSKU);

            assertEquals(validSKU, sku.value());
        }
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By Value")
    void shouldSupportEqualsAndHashCodeByValue() {
        SKU sku1 = new SKU("LAPTOP-001");
        SKU sku2 = new SKU("LAPTOP-001");

        assertEquals(sku1, sku2);
        assertEquals(sku1.hashCode(), sku2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When Values Differ")
    void shouldNotBeEqualWhenValuesDiffer() {
        SKU sku1 = new SKU("LAPTOP-001");
        SKU sku2 = new SKU("LAPTOP-002");

        assertNotEquals(sku1, sku2);
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        SKU sku = new SKU("LAPTOP-001");

        assertNotNull(sku.toString());
        assertFalse(sku.toString().isEmpty());
        assertTrue(sku.toString().contains("LAPTOP-001"));
    }
}
