package com.debuggeandoideas.erp_lite.domain.entities.product;

/**
 * Reference to Catalog in MongoDB.
 * Example: cat-electronics, cat-books
 *
 * @param categoryId the category identifier
 */
public record CategoryReference(String categoryId) {

    public CategoryReference {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
    }

    /**
     * Creates a CategoryReference from a String value.
     *
     * @param categoryId the category identifier
     * @return a new CategoryReference instance
     */
    public static CategoryReference of(String categoryId) {
        return new CategoryReference(categoryId);
    }
}
