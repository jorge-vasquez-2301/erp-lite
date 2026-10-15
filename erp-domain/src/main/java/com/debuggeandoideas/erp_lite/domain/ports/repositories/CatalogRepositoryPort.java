package com.debuggeandoideas.erp_lite.domain.ports.repositories;

import com.debuggeandoideas.erp_lite.domain.entities.catalog.CatalogItem;
import com.debuggeandoideas.erp_lite.domain.entities.catalog.CatalogType;

import javax.xml.catalog.Catalog;
import java.util.List;
import java.util.Optional;

/**
 * Port read-only for Catalog
 */
public interface CatalogRepositoryPort {

    Optional<Catalog> findByType(CatalogType type);

    List<CatalogItem> findItemsByType(CatalogType type);

    Optional<CatalogItem> findItemByTypeAndCode(CatalogType type, String code);
}
