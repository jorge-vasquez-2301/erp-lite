package com.debuggeandoideas.erp_lite.persistence.jpa.adapters;

import com.debuggeandoideas.erp_lite.domain.entities.product.ProductId;
import com.debuggeandoideas.erp_lite.domain.entities.product.ProductRoot;
import com.debuggeandoideas.erp_lite.domain.ports.repositories.ProductRepositoryPort;
import com.debuggeandoideas.erp_lite.persistence.jpa.entities.ProductEntity;
import com.debuggeandoideas.erp_lite.persistence.jpa.mappers.ProductJpaMapper;
import com.debuggeandoideas.erp_lite.persistence.jpa.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;
    private final ProductJpaMapper productJpaMapper;


    @Override
    public ProductRoot save(ProductRoot product) {
        log.info("Saving product {}", product);
        try {
            ProductEntity productEntity = this.productJpaMapper.toEntity(product);

            productEntity.setId(product.getId().value());

            log.info("try to saving product {}", product.getSku());

            ProductEntity productSaved = this.productRepository.save(productEntity);

            log.info("saved product SUCCESS {}", productSaved);

            return this.productJpaMapper.toDomain(productSaved);
        } catch (Exception e) {
            log.error("Error on persist product", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<ProductRoot> findAllById(ProductId id) {
        log.debug("Finding product by ID: {}", id.value());

        try {
            Optional<ProductEntity> entityOpt = this.productRepository.findById(id.value());

            if (entityOpt.isEmpty()) {
                log.debug("Product not found with ID: {}", id.value());
                return Optional.empty();
            }

            ProductEntity entity = entityOpt.get();
            log.debug("Product found with ID: {} and SKU: {}",
                    entity.getId(),
                    entity.getSku());

            ProductRoot domain = this.productJpaMapper.toDomain(entity);

            log.debug("Product rehydrated successfully");

            return Optional.of(domain);

        } catch (Exception e) {
            log.error("Failed to find product by ID: {}", id.value(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProductRoot> findBySku(String sku) {
        log.debug("Finding product by SKU: {}", sku);

        try {
            Optional<ProductEntity> entityOpt = this.productRepository.findBySku(sku);

            if (entityOpt.isEmpty()) {
                log.debug("Product not found with SKU: {}", sku);
                return Optional.empty();
            }

            ProductEntity entity = entityOpt.get();
            log.debug("Product found with SKU: {} (ID: {})",
                    entity.getSku(),
                    entity.getId());

            ProductRoot domain = this.productJpaMapper.toDomain(entity);

            return Optional.of(domain);

        } catch (Exception e) {
            log.error("Failed to find product by SKU: {}", sku, e);
            return Optional.empty();
        }
    }

    @Override
    public void delete(ProductRoot product) {
        log.info("Deleting product with ID: {}", product.getId().value());

        try {
            this.productRepository.deleteById(product.getId().value());
            log.info("Product deleted successfully with ID: {}", product.getId().value());

        } catch (Exception e) {
            log.error("Failed to delete product with ID: {}", product.getId().value(), e);
            throw new IllegalStateException("Failed to delete product: " + e.getMessage(), e);
        }
    }
}