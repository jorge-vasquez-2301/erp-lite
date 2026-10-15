package com.debuggeandoideas.erp_lite.domain.entities.product;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Product image URL stored in AWS S3.
 * Validates that the URL is well-formed.
 *
 * @param imageUrl the image URL
 */
public record ProductImage(String imageUrl) {

    public ProductImage {
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image URL cannot be null");
        }
        validateUrl(imageUrl);
    }

    private static void validateUrl(String url) {
        try {
            URI uri = new URI(url);
            if (!uri.isAbsolute()) {
                throw new IllegalArgumentException("URL must be absolute: " + url);
            }
            if (uri.getScheme() == null || (!uri.getScheme().equals("http") && !uri.getScheme().equals("https"))) {
                throw new IllegalArgumentException("URL must use http or https scheme: " + url);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format: " + url, e);
        }
    }

    /**
     * Creates a ProductImage from a String URL.
     *
     * @param imageUrl the image URL
     * @return a new ProductImage instance
     */
    public static ProductImage of(String imageUrl) {
        return new ProductImage(imageUrl);
    }

    /**
     * Returns the full URL.
     *
     * @return the image URL
     */
    public String getFullUrl() {
        return imageUrl;
    }

    /**
     * Extracts the filename from the URL.
     *
     * @return the filename or the full URL if extraction fails
     */
    public String getFileName() {
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < imageUrl.length() - 1) {
            return imageUrl.substring(lastSlashIndex + 1);
        }
        return imageUrl;
    }
}
