package com.debuggeandoideas.erp_lite.domain.ports.repositories;

import com.debuggeandoideas.erp_lite.domain.views.ProductView;

import java.util.List;
import java.util.Optional;

/**
 * Port read-only for Products in Catalog
 */
public interface ProductCatalogRepositoryPort {

    Optional<ProductView> findById(String id);

    Optional<ProductView> findBySku(String sku);

    List<ProductView> findByText(String text);

    List<ProductView> findByCategory(String category);

    List<ProductView> findActive();
}