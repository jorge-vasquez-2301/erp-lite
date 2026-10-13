package com.debuggeandoideas.erp_lite.domain.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * Immutable monetary value with currency.
 * Represents money with amount and currency code.
 *
 * @param amount   the monetary amount (must be >= 0)
 * @param currency the currency code
 */
public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        // Scale to 2 decimal places for consistency
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Creates a Money instance from BigDecimal and Currency.
     *
     * @param amount   the monetary amount
     * @param currency the currency
     * @return a new Money instance
     */
    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    /**
     * Creates a Money instance from double and Currency.
     *
     * @param amount   the monetary amount
     * @param currency the currency
     * @return a new Money instance
     */
    public static Money of(double amount, Currency currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    /**
     * Adds another Money value to this one.
     * Both Money objects must have the same currency.
     *
     * @param other the Money to add
     * @return a new Money instance with the sum
     * @throws IllegalArgumentException if currencies don't match
     */
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies: " + this.currency + " and " + other.currency);
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Subtracts another Money value from this one.
     * Both Money objects must have the same currency.
     *
     * @param other the Money to subtract
     * @return a new Money instance with the difference
     * @throws IllegalArgumentException if currencies don't match or result is negative
     */
    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies: " + this.currency + " and " + other.currency);
        }
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Subtraction result cannot be negative");
        }
        return new Money(result, this.currency);
    }

    /**
     * Multiplies this Money by an integer multiplier.
     *
     * @param multiplier the multiplier
     * @return a new Money instance with the product
     */
    public Money multiply(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Multiplier cannot be negative");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
    }

    /**
     * Multiplies this Money by a Quantity.
     *
     * @param quantity the quantity to multiply by
     * @return a new Money instance with the product
     */
    public Money multiply(Quantity quantity) {
        return this.multiply(quantity.value());
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }
}
