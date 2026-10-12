package com.debuggeandoideas.erp_lite.persistence.mongo.documents;

public record ProductSpecifications(
        String processor,
        String ram,
        String storage,
        String display,
        String weight
) {}