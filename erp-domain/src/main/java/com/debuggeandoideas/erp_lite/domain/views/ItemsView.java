package com.debuggeandoideas.erp_lite.domain.views;

public record ItemsView(
        String code,
        String value,
        String description,
        Integer displayOrder
) {
}