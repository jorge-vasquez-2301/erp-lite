package com.debuggeandoideas.erp_lite.use_cases.product;

import com.debuggeandoideas.erp_lite.commands.product.CreateProductCommand;
import com.debuggeandoideas.erp_lite.domain.entities.product.*;
import com.debuggeandoideas.erp_lite.domain.ports.repositories.ProductRepositoryPort;
import com.debuggeandoideas.erp_lite.domain.ports.services.ImageStorageServicePort;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import com.debuggeandoideas.erp_lite.exceptions.CommandException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;

/**
 * Use Case: Create a new product.
 * JIRA TICKET: ERP-6735
 * 1. Validate SKU uniqueness
 * 2. Upload image to S3 (if provided)
 * 3. Create ProductRoot aggregate
 * 4. Persist to PostgreSQL
 * 5. Publish ProductCreated event (CQRS sync to MongoDB)
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepositoryPort productRepository;
    private final ImageStorageServicePort imageStorageService;


    public String execute(CreateProductCommand command) {
        log.info("Creating product with SKU: {}", command.sku());

        try {
            // 1. Validate SKU uniqueness
            validateSkuUniqueness(command.sku());

            // 2. Upload image (if provided)


            // 3. Create value objects
            SKU sku = SKU.of(command.sku());
            ProductName name = ProductName.of(command.name());
            Money price = Money.of(command.price(), Currency.getInstance(command.currency()));
            Stock stock = Stock.of(command.stock());
            CategoryReference category = CategoryReference.of(command.categoryId());

            ProductImage img = this.uploadImg(command);

            // 4. Create product aggregate
            ProductRoot product = ProductRoot.create(
                    sku,
                    name,
                    command.description(),
                    price,
                    stock,
                    category,
                    img,
                    command.createdBy()
            );

            log.debug("Product created in domain with ID: {}", product.getId().value());

            // 5. Persist product
            ProductRoot savedProduct = productRepository.save(product);

            log.info("Product persisted with ID: {}", savedProduct.getId().value());

            // TODO: Handle domain events - Sync to MongoDB

            return savedProduct.getId().value().toString();

        } catch (IllegalArgumentException iae) {
            log.error("Invalid data for product creation");
            throw new CommandException("Error creating product: " + iae.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating product", e);
            throw new CommandException("Failed to create product: " + e.getMessage());
        }
    }

    private void validateSkuUniqueness(String sku) {
        log.debug("Validating SKU uniqueness: {}", sku);

        if (productRepository.findBySku(sku).isPresent()) {
            log.warn("SKU already exists: {}", sku);
            throw new CommandException("Product with SKU '" + sku + "' already exists");
        }
    }

    private ProductImage uploadImg(CreateProductCommand command) {
        if (!command.hasImage()) {
            log.info("Product image is empty");
            return null;
        }

        log.info("Uploading image with SKU: {}", command.sku());

        try {
            return this.imageStorageService.upload(
                    command.imageName(),
                    command.imageData()
            );
        } catch (Exception e) {
            log.error("Unexpected error uploading image with SKU", e);
            throw new CommandException("Error uploading image with SKU: " + e.getMessage());
        }
    }
}