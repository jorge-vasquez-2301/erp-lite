package com.debuggeandoideas.erp_lite.domain.catalog;

import lombok.Getter;

/**
 * Types of catalogs available in the system.
 * These values match the catalogType field in MongoDB catalogs collection.
 */
@Getter
public enum CatalogType {

    PRODUCT_CATEGORIES("PRODUCT_CATEGORIES", "Product Categories"),
    ORDER_STATUSES("ORDER_STATUSES", "Order Statuses"),
    PAYMENT_METHODS("PAYMENT_METHODS", "Payment Methods"),
    SHIPPING_METHODS("SHIPPING_METHODS", "Shipping Methods"),
    COUNTRIES("COUNTRIES", "Countries"),
    CURRENCIES("CURRENCIES", "Currencies");

    private final String code;
    private final String displayName;

    CatalogType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Convert string code to enum (case-sensitive)
     * @throws IllegalArgumentException if code is invalid
     */
    public static CatalogType fromCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Catalog type code cannot be null or empty");
        }

        for (CatalogType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }

        throw new IllegalArgumentException(
                "Invalid catalog type code: '" + code + "'. " +
                        "Valid values are: PRODUCT_CATEGORIES, ORDER_STATUSES, " +
                        "PAYMENT_METHODS, SHIPPING_METHODS, COUNTRIES, CURRENCIES"
        );
    }

    public static boolean isValid(String code) {
        try {
            fromCode(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
