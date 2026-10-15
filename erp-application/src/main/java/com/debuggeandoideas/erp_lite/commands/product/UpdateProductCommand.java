package com.debuggeandoideas.erp_lite.commands.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Command to update an existing product.
 *
 * @param productId   Product ID to update
 * @param name        New product name (optional - null means no change)
 * @param description New product description (optional)
 * @param price       New product price (optional - null means no change)
 * @param categoryId  New category ID (optional - null means no change)
 * @param imageData   New image binary data (optional)
 * @param imageName   New image file name (optional)
 */
public record UpdateProductCommand(

        @NotBlank(message = "Product ID cannot be null or blank")
        String productId,

        @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
        String name,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
        @Digits(integer = 13, fraction = 2, message = "Price format invalid (max 13 digits, 2 decimals)")
        BigDecimal price,

        String categoryId,

        byte[] imageData,

        String imageName
) {
    /**
     * Custom validations in compact constructor
     */
    public UpdateProductCommand {
        // If imageData is provided, imageName must also be provided
        if (imageData != null && imageData.length > 0 &&
                (imageName == null || imageName.isBlank())) {
            throw new IllegalArgumentException("Image name is required when image data is provided");
        }

        // At least one field must be provided for update
        if (name == null && description == null && price == null &&
                categoryId == null && imageData == null) {
            throw new IllegalArgumentException("At least one field must be provided for update");
        }
    }

    /**
     * Check if this command includes an image upload.
     */
    public boolean hasImage() {
        return imageData != null && imageData.length > 0;
    }

    /**
     * Check if name should be updated.
     */
    public boolean shouldUpdateName() {
        return name != null && !name.isBlank();
    }

    /**
     * Check if price should be updated.
     */
    public boolean shouldUpdatePrice() {
        return price != null;
    }

    /**
     * Check if category should be updated.
     */
    public boolean shouldUpdateCategory() {
        return categoryId != null && !categoryId.isBlank();
    }
}