package com.debuggeandoideas.erp_lite.shared;

import com.debuggeandoideas.erp_lite.domain.shared.Money;
import com.debuggeandoideas.erp_lite.domain.shared.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money Domain Test")
class MoneyTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Amount Is Null")
    void shouldThrowIllegalArgumentExceptionWhenAmountIsNull() {
        final String msgEx = "Amount cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Money(null, USD));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Amount Is Negative")
    void shouldThrowIllegalArgumentExceptionWhenAmountIsNegative() {
        final String msgEx = "Amount cannot be negative";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Money(BigDecimal.valueOf(-10.0), USD));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Currency Is Null")
    void shouldThrowIllegalArgumentExceptionWhenCurrencyIsNull() {
        final String msgEx = "Currency cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Money(BigDecimal.TEN, null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Create Money With Valid Data And Scale To 2 Decimals")
    void shouldCreateMoneyWithValidDataAndScaleTo2Decimals() {
        Money money = new Money(BigDecimal.valueOf(10.999), USD);

        assertEquals(BigDecimal.valueOf(11.00).setScale(2), money.amount());
        assertEquals(USD, money.currency());
    }

    @Test
    @DisplayName("Should Create Money Using of Method With BigDecimal")
    void shouldCreateMoneyUsingOfMethodWithBigDecimal() {
        Money money = Money.of(BigDecimal.valueOf(50.5), USD);

        assertEquals(BigDecimal.valueOf(50.50).setScale(2), money.amount());
        assertEquals(USD, money.currency());
    }

    @Test
    @DisplayName("Should Create Money Using of Method With Double")
    void shouldCreateMoneyUsingOfMethodWithDouble() {
        Money money = Money.of(99.99, USD);

        assertEquals(BigDecimal.valueOf(99.99).setScale(2), money.amount());
        assertEquals(USD, money.currency());
    }

    @Test
    @DisplayName("Should Add Two Money Values With Same Currency")
    void shouldAddTwoMoneyValuesWithSameCurrency() {
        Money money1 = Money.of(10.50, USD);
        Money money2 = Money.of(5.25, USD);

        Money result = money1.add(money2);

        assertEquals(BigDecimal.valueOf(15.75).setScale(2), result.amount());
        assertEquals(USD, result.currency());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Adding Different Currencies")
    void shouldThrowIllegalArgumentExceptionWhenAddingDifferentCurrencies() {
        Money money1 = Money.of(10.50, USD);
        Money money2 = Money.of(5.25, EUR);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> money1.add(money2));

        assertTrue(targetEx.getMessage().contains("Cannot add money with different currencies"));
    }

    @Test
    @DisplayName("Should Subtract Two Money Values With Same Currency")
    void shouldSubtractTwoMoneyValuesWithSameCurrency() {
        Money money1 = Money.of(10.50, USD);
        Money money2 = Money.of(5.25, USD);

        Money result = money1.subtract(money2);

        assertEquals(BigDecimal.valueOf(5.25).setScale(2), result.amount());
        assertEquals(USD, result.currency());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Subtracting Different Currencies")
    void shouldThrowIllegalArgumentExceptionWhenSubtractingDifferentCurrencies() {
        Money money1 = Money.of(10.50, USD);
        Money money2 = Money.of(5.25, EUR);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> money1.subtract(money2));

        assertTrue(targetEx.getMessage().contains("Cannot subtract money with different currencies"));
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Subtraction Result Is Negative")
    void shouldThrowIllegalArgumentExceptionWhenSubtractionResultIsNegative() {
        Money money1 = Money.of(5.25, USD);
        Money money2 = Money.of(10.50, USD);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> money1.subtract(money2));

        assertEquals("Subtraction result cannot be negative", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Multiply Money By Integer")
    void shouldMultiplyMoneyByInteger() {
        Money money = Money.of(10.50, USD);

        Money result = money.multiply(3);

        assertEquals(BigDecimal.valueOf(31.50).setScale(2), result.amount());
        assertEquals(USD, result.currency());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Multiplier Is Negative")
    void shouldThrowIllegalArgumentExceptionWhenMultiplierIsNegative() {
        Money money = Money.of(10.50, USD);

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> money.multiply(-1));

        assertEquals("Multiplier cannot be negative", targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Multiply Money By Quantity")
    void shouldMultiplyMoneyByQuantity() {
        Money money = Money.of(10.50, USD);
        Quantity quantity = Quantity.of(5);

        Money result = money.multiply(quantity);

        assertEquals(BigDecimal.valueOf(52.50).setScale(2), result.amount());
        assertEquals(USD, result.currency());
    }

    @Test
    @DisplayName("Should Return Correct String Representation")
    void shouldReturnCorrectStringRepresentation() {
        Money money = Money.of(10.50, USD);

        String result = money.toString();

        assertEquals("10.50 USD", result);
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By Amount And Currency")
    void shouldSupportEqualsAndHashCodeByAmountAndCurrency() {
        Money money1 = Money.of(10.50, USD);
        Money money2 = Money.of(10.50, USD);

        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When Amounts Differ")
    void shouldNotBeEqualWhenAmountsDiffer() {
        Money money1 = Money.of(10.50, USD);
        Money money2 = Money.of(20.00, USD);

        assertNotEquals(money1, money2);
    }

    @Test
    @DisplayName("Should Not Be Equal When Currencies Differ")
    void shouldNotBeEqualWhenCurrenciesDiffer() {
        Money money1 = Money.of(10.50, USD);
        Money money2 = Money.of(10.50, EUR);

        assertNotEquals(money1, money2);
    }

    @Test
    @DisplayName("Should Allow Zero Amount")
    void shouldAllowZeroAmount() {
        Money money = Money.of(0.0, USD);

        assertEquals(BigDecimal.ZERO.setScale(2), money.amount());
        assertEquals(USD, money.currency());
    }
}
