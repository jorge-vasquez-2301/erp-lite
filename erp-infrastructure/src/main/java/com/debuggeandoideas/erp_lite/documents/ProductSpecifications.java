package com.debuggeandoideas.erp_lite.documents;

public record ProductSpecifications(
        String processor,
        String ram,
        String storage,
        String display,
        String weight
) {}