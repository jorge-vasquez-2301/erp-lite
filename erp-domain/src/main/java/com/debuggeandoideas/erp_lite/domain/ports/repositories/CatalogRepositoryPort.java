package com.debuggeandoideas.erp_lite.domain.ports.repositories;


import com.debuggeandoideas.erp_lite.domain.views.CatalogView;
import com.debuggeandoideas.erp_lite.domain.views.ItemsView;
import com.debuggeandoideas.erp_lite.enums.CatalogType;

import java.util.List;
import java.util.Optional;

/**
 * Port read-only for Catalog
 */
public interface CatalogRepositoryPort {

    Optional<CatalogView> findByType(CatalogType type);

    List<ItemsView> findItemsByType(CatalogType type);

    Optional<ItemsView> findItemByTypeAndCode(CatalogType type, String code);
}