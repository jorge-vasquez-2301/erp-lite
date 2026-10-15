package com.debuggeandoideas.erp_lite.domain.ports.repositories;

import com.debuggeandoideas.erp_lite.domain.entities.product.ProductId;
import com.debuggeandoideas.erp_lite.domain.entities.product.ProductRoot;

import java.util.Optional;

/**
 * Port for storage o consult Products
 */
public interface ProductRepositoryPort {

    ProductRoot save(ProductRoot productRoot);

    Optional<ProductRoot> findAllById(ProductId id);

    Optional<ProductRoot> findBySku(String sku);

    void delete(ProductRoot productRoot);
}