package com.debuggeandoideas.erp_lite.use_cases.product;

import com.debuggeandoideas.erp_lite.commands.product.UpdateProductCommand;
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
import java.util.UUID;

/**
 * Use Case: Update product information.
 * JIRA TICKET: ERP-6737
 * 1. Find product by ID
 * 2. Upload new image to S3 (if provided)
 * 3. Delete old image from S3 (if replaced)
 * 4. Update product information
 * 5. Persist changes
 * 6. Publish ProductUpdated event
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepositoryPort productRepository;
    private final ImageStorageServicePort imageStorageService;

    public void execute(UpdateProductCommand command) {
        log.info("Updating product: {}", command.productId());

        try {
            // 1. Find product
            ProductRoot product = findProductById(command.productId());

            log.debug("Current product: SKU={}, Name={}",
                    product.getSku().value(),
                    product.getName().value());

            // 2. Handle image update (if provided)
            ProductImage oldImage = product.getImage();
            ProductImage newImage = updateImage(command, oldImage);

            // 3. Build updated values
            ProductName name = command.shouldUpdateName()
                    ? ProductName.of(command.name())
                    : product.getName();

            String description = command.description() != null
                    ? command.description()
                    : product.getDescription();

            Money price = command.shouldUpdatePrice()
                    ? Money.of(command.price(), Currency.getInstance(product.getPrice().currency().getCurrencyCode()))
                    : product.getPrice();

            CategoryReference category = command.shouldUpdateCategory()
                    ? CategoryReference.of(command.categoryId())
                    : product.getCategory();

            ProductImage finalImage = newImage != null ? newImage : product.getImage();

            // 4. Update product
            product.update(name, description, price, category, finalImage);

            log.debug("Product updated in domain");

            // 5. Persist changes
            productRepository.save(product);

            log.info("Product update persisted");

        } catch (IllegalArgumentException iae) {
            log.error("Invalid data for product update", iae);
            throw new CommandException("Error updating product: " + iae.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error updating product", e);
            throw new CommandException("Failed to update product: " + e.getMessage());
        }
    }

    private ProductRoot findProductById(String productId) {
        log.debug("Finding product by ID: {}", productId);

        ProductId productIdVO = ProductId.of(UUID.fromString(productId));

        return productRepository.findAllById(productIdVO)
                .orElseThrow(() -> {
                    log.warn("Product not found: {}", productId);
                    return new CommandException("Product not found with ID: " + productId);
                });
    }

    private ProductImage updateImage(UpdateProductCommand command, ProductImage oldImage) {
        if (!command.hasImage()) {
            log.debug("No image update requested");
            return null;
        }

        log.debug("Uploading new image: {}", command.imageName());

        try {
            // Upload new image
            ProductImage newImage = imageStorageService.upload(
                    command.imageName(),
                    command.imageData()
            );

            log.info("New image uploaded: {}", newImage.imageUrl());

            // Delete old image (if exists)
            if (oldImage != null) {

                imageStorageService.delete(oldImage);
                log.debug("Old image deleted: {}", oldImage.imageUrl());
            }

            return newImage;

        } catch (Exception e) {
            log.error("Failed to upload new image", e);
            throw new CommandException("Failed to upload product image: " + e.getMessage());
        }
    }


}