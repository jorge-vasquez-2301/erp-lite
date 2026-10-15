package com.debuggeandoideas.erp_lite.use_cases.product;

import com.debuggeandoideas.erp_lite.commands.product.UpdateStockCommand;
import com.debuggeandoideas.erp_lite.domain.entities.product.ProductId;
import com.debuggeandoideas.erp_lite.domain.entities.product.ProductRoot;
import com.debuggeandoideas.erp_lite.domain.ports.repositories.ProductRepositoryPort;
import com.debuggeandoideas.erp_lite.exceptions.CommandException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case: Update product stock
 * JIRA TICKET: ERP-6738
 * 1. Find product by ID
 * 2. Increment or decrement stock
 * 3. Persist changes
 * 4. Publish StockChanged event
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UpdateStockUseCase {

    private final ProductRepositoryPort productRepository;

    public void execute(UpdateStockCommand command) {
        log.info("Updating stock for product: {} by {} units (reason: {})",
                command.productId(),
                command.quantity(),
                command.reason());

        try {
            // 1. Find product
            ProductRoot product = findProductById(command.productId());

            log.debug("Current stock: {}", product.getStock().value());

            // 2. Update stock based on operation
            if (command.isIncrement()) {
                product.incrementStock(command.absoluteQuantity(), command.reason());
                log.debug("Stock incremented by {} units", command.absoluteQuantity());
            } else if (command.isDecrement()) {
                product.decrementStock(command.absoluteQuantity(), command.reason());
                log.debug("Stock decremented by {} units", command.absoluteQuantity());
            }

            log.debug("New stock: {}", product.getStock().value());

            // 3. Persist changes
            productRepository.save(product);

            log.info("Stock update persisted. New stock: {}", product.getStock().value());

        } catch (IllegalArgumentException iae) {
            log.error("Invalid stock update", iae);
            throw new CommandException("Error updating stock: " + iae.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error updating stock", e);
            throw new CommandException("Failed to update stock: " + e.getMessage());
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
}