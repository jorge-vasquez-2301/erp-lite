package com.debuggeandoideas.erp_lite.persistence.mongo.adapters;

import com.debuggeandoideas.erp_lite.domain.ports.repositories.ProductCatalogRepositoryPort;
import com.debuggeandoideas.erp_lite.domain.views.ProductView;
import com.debuggeandoideas.erp_lite.persistence.mongo.mappers.ProductCatalogMapper;
import com.debuggeandoideas.erp_lite.persistence.mongo.repositories.ProductInCatalogRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.debuggeandoideas.erp_lite.constants.CacheConstants.*;

@Repository
@Slf4j
@AllArgsConstructor
public class ProductCatalogRepositoryAdapter implements ProductCatalogRepositoryPort {

    private final ProductInCatalogRepository productInCatalogRepository;
    private final ProductCatalogMapper productCatalogMapper;
    private final CacheManager cacheManager;

    @Override
    public Optional<ProductView> findById(String id) {
        Cache cache = cacheManager.getCache(CACHE_PRODUCTS_BY_ID);
        if (cache != null) {
            ProductView cached = cache.get(id, ProductView.class);
            if (cached != null) {
                log.debug("Cache HIT for productId: {}", id);
                return Optional.of(cached);
            }
        }

        return this.productInCatalogRepository.findById(id)
                .map(productCatalogMapper::toView);
    }

    @Override
    public Optional<ProductView> findBySku(String sku) {
        Cache cache = cacheManager.getCache(CACHE_PRODUCTS_BY_SKU);
        if (cache != null) {
            ProductView cached = cache.get(sku, ProductView.class);
            if (cached != null) {
                log.debug("Cache HIT for sku: {}", sku);
                return Optional.of(cached);
            }
        }

        return this.productInCatalogRepository.findBySku(sku)
                .map(productCatalogMapper::toView);
    }

    @Override
    public List<ProductView> findByText(String text) {
        log.info("Find product by text: {}", text);

        return this.productInCatalogRepository.findByTextAndActive(text)
                .stream().map(productCatalogMapper::toView)
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductView> findByCategory(String category) {
        Cache cache = cacheManager.getCache(CACHE_PRODUCTS_BY_CATEGORY);
        if (cache != null) {
            List<ProductView> cached = cache.get(category, List.class);
            if (cached != null) {
                log.debug("Cache HIT for category: {}", category);
                return cached;
            }
        }

        return this.productInCatalogRepository.findByCategoryIdAndActiveTrue(category)
                .stream().map(productCatalogMapper::toView)
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductView> findActive() {
        Cache cache = cacheManager.getCache(CACHE_PRODUCTS_ACTIVE);
        if (cache != null) {
            List<ProductView> cached = cache.get("all", List.class);
            if (cached != null) {
                log.debug("Cache for active products");
                return cached;
            }
        }

        return this.productInCatalogRepository.findByActiveTrueOrderByIdAsc()
                .stream().map(productCatalogMapper::toView)
                .toList();
    }
}