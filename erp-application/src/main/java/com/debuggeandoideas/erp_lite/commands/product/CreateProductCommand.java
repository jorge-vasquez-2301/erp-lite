package com.debuggeandoideas.erp_lite.commands.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Command to create a new product.
 *
 * @param sku         Product SKU (unique identifier)
 * @param name        Product name
 * @param description Product description (optional)
 * @param price       Product price (must be > 0)
 * @param currency    Currency code (e.g., "USD", "MXN")
 * @param stock       Initial stock quantity (must be >= 0)
 * @param categoryId  Category identifier (reference to MongoDB)
 * @param imageData   Image binary data (optional)
 * @param imageName   Image file name (optional)
 * @param createdBy   Username of the user creating the product
 */
public record CreateProductCommand(

        @NotBlank(message = "SKU cannot be null or blank")
        String sku,

        @NotBlank(message = "Product name cannot be null or blank")
        @Size(min = 3, max = 50, message = "Product name must be between 3 and 50 characters")
        String name,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
        @Digits(integer = 13, fraction = 2, message = "Price format invalid (max 13 digits, 2 decimals)")
        BigDecimal price,

        @NotBlank(message = "Currency cannot be null or blank")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code (e.g., USD, MXN, EUR)")
        String currency,

        @NotNull(message = "Stock cannot be null")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock,

        @NotBlank(message = "Category ID cannot be null or blank")
        String categoryId,

        byte[] imageData,

        String imageName,

        @NotBlank(message = "Created by cannot be null or blank")
        String createdBy
) {

    /**
     * Custom validation: If imageData is provided, imageName must also be provided
     */
    public CreateProductCommand {
        if (imageData != null && imageData.length > 0 &&
                (imageName == null || imageName.isBlank())) {
            throw new IllegalArgumentException("Image name is required when image data is provided");
        }
    }

    /**
     * Check if this command includes an image upload.
     */
    public boolean hasImage() {
        return imageData != null && imageData.length > 0;
    }
}