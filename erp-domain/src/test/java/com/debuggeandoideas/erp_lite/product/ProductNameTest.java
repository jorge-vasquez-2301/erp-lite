package com.debuggeandoideas.erp_lite.product;

import com.debuggeandoideas.erp_lite.domain.entities.product.ProductName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductName Domain Test")
class ProductNameTest {

    @Test
    @DisplayName("Should Throw IllegalArgumentException When ProductName Is Null")
    void shouldThrowIllegalArgumentExceptionWhenProductNameIsNull() {
        final String msgEx = "Product name cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new ProductName(null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When ProductName Is Too Short")
    void shouldThrowIllegalArgumentExceptionWhenProductNameIsTooShort() {
        String[] shortNames = {"", "A", "AB"};

        for (String shortName : shortNames) {
            IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                    () -> new ProductName(shortName),
                    "Should throw exception for: '" + shortName + "'");

            assertTrue(targetEx.getMessage().contains("must have at least 3 characters"),
                    "Exception message should contain 'must have at least 3 characters' for: " + shortName);
        }
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When ProductName Is Too Long")
    void shouldThrowIllegalArgumentExceptionWhenProductNameIsTooLong() {
        String longName = "A".repeat(201);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new ProductName(longName));

        assertTrue(targetEx.getMessage().contains("cannot exceed 200 characters"));
    }

    @Test
    @DisplayName("Should Create ProductName With Valid Length")
    void shouldCreateProductNameWithValidLength() {
        String validName = "Laptop Computer";

        ProductName productName = new ProductName(validName);

        assertEquals(validName, productName.value());
    }

    @Test
    @DisplayName("Should Create ProductName Using of Method")
    void shouldCreateProductNameUsingOfMethod() {
        String validName = "Wireless Mouse";

        ProductName productName = ProductName.of(validName);

        assertEquals(validName, productName.value());
    }

    @Test
    @DisplayName("Should Accept ProductName With Minimum Length")
    void shouldAcceptProductNameWithMinimumLength() {
        String minName = "ABC";

        ProductName productName = assertDoesNotThrow(() -> new ProductName(minName));

        assertEquals(minName, productName.value());
    }

    @Test
    @DisplayName("Should Accept ProductName With Maximum Length")
    void shouldAcceptProductNameWithMaximumLength() {
        String maxName = "A".repeat(200);

        ProductName productName = assertDoesNotThrow(() -> new ProductName(maxName));

        assertEquals(maxName, productName.value());
    }

    @Test
    @DisplayName("Should Accept ProductName With Special Characters")
    void shouldAcceptProductNameWithSpecialCharacters() {
        String[] validNames = {
                "Laptop & Desktop",
                "Mouse (Wireless)",
                "Keyboard - Mechanical",
                "Monitor 27\"",
                "Product: Test 123"
        };

        for (String validName : validNames) {
            ProductName productName = assertDoesNotThrow(() -> new ProductName(validName),
                    "Should not throw exception for: " + validName);

            assertEquals(validName, productName.value());
        }
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By Value")
    void shouldSupportEqualsAndHashCodeByValue() {
        ProductName productName1 = new ProductName("Laptop Computer");
        ProductName productName2 = new ProductName("Laptop Computer");

        assertEquals(productName1, productName2);
        assertEquals(productName1.hashCode(), productName2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When Values Differ")
    void shouldNotBeEqualWhenValuesDiffer() {
        ProductName productName1 = new ProductName("Laptop Computer");
        ProductName productName2 = new ProductName("Desktop Computer");

        assertNotEquals(productName1, productName2);
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        ProductName productName = new ProductName("Laptop Computer");

        assertNotNull(productName.toString());
        assertFalse(productName.toString().isEmpty());
        assertTrue(productName.toString().contains("Laptop Computer"));
    }
}
