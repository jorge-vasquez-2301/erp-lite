package com.debuggeandoideas.erp_lite.domain.views;

import java.util.List;
import java.util.Map;

/**
 * Read model representation of a Product.
 * Used for queries (CQRS read side).
 * This is a simplified view optimized for display.
 */
public record ProductView(
        String sku,
        String name,
        String description,
        Double price,
        String money,
        Integer stock,
        String imageUrl,
        List<String> tags,
        Map<String, Object> specifications) {
}