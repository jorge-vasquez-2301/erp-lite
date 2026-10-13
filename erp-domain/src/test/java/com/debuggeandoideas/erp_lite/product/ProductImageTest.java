package com.debuggeandoideas.erp_lite.product;

import com.debuggeandoideas.erp_lite.domain.product.ProductImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductImage Domain Test")
class ProductImageTest {

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Image URL Is Null")
    void shouldThrowIllegalArgumentExceptionWhenImageURLIsNull() {
        final String msgEx = "Image URL cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new ProductImage(null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When URL Format Is Invalid")
    void shouldThrowIllegalArgumentExceptionWhenURLFormatIsInvalid() {
        String[] invalidURLs = {
                "not a url",
                "ftp://example.com/image.jpg",
                "file:///local/path/image.jpg",
                "image.jpg",
                "//example.com/image.jpg",
                ""
        };

        for (String invalidURL : invalidURLs) {
            IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                    () -> new ProductImage(invalidURL),
                    "Should throw exception for: " + invalidURL);

            assertTrue(targetEx.getMessage().contains("Invalid URL") ||
                       targetEx.getMessage().contains("URL must"),
                    "Exception message should indicate invalid URL for: " + invalidURL);
        }
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When URL Is Not Absolute")
    void shouldThrowIllegalArgumentExceptionWhenURLIsNotAbsolute() {
        String relativeURL = "/images/product.jpg";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new ProductImage(relativeURL));

        assertTrue(targetEx.getMessage().contains("URL must be absolute"));
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When URL Scheme Is Not HTTP Or HTTPS")
    void shouldThrowIllegalArgumentExceptionWhenURLSchemeIsNotHTTPOrHTTPS() {
        String ftpURL = "ftp://example.com/image.jpg";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new ProductImage(ftpURL));

        assertTrue(targetEx.getMessage().contains("URL must use http or https scheme"));
    }

    @Test
    @DisplayName("Should Create ProductImage With Valid HTTP URL")
    void shouldCreateProductImageWithValidHTTPURL() {
        String validURL = "http://example.com/images/product.jpg";

        ProductImage productImage = new ProductImage(validURL);

        assertEquals(validURL, productImage.imageUrl());
    }

    @Test
    @DisplayName("Should Create ProductImage With Valid HTTPS URL")
    void shouldCreateProductImageWithValidHTTPSURL() {
        String validURL = "https://example.com/images/product.jpg";

        ProductImage productImage = new ProductImage(validURL);

        assertEquals(validURL, productImage.imageUrl());
    }

    @Test
    @DisplayName("Should Create ProductImage Using of Method")
    void shouldCreateProductImageUsingOfMethod() {
        String validURL = "https://s3.amazonaws.com/bucket/image.jpg";

        ProductImage productImage = ProductImage.of(validURL);

        assertEquals(validURL, productImage.imageUrl());
    }

    @Test
    @DisplayName("Should Accept Valid S3 URLs")
    void shouldAcceptValidS3URLs() {
        String[] validS3URLs = {
                "https://s3.amazonaws.com/my-bucket/images/product1.jpg",
                "https://my-bucket.s3.amazonaws.com/images/product2.png",
                "https://s3.us-east-1.amazonaws.com/bucket/folder/image.webp"
        };

        for (String validURL : validS3URLs) {
            ProductImage productImage = assertDoesNotThrow(() -> new ProductImage(validURL),
                    "Should not throw exception for: " + validURL);

            assertEquals(validURL, productImage.imageUrl());
        }
    }

    @Test
    @DisplayName("Should Return Full URL Using getFullUrl Method")
    void shouldReturnFullURLUsingGetFullUrlMethod() {
        String validURL = "https://example.com/images/product.jpg";
        ProductImage productImage = new ProductImage(validURL);

        String fullUrl = productImage.getFullUrl();

        assertEquals(validURL, fullUrl);
    }

    @Test
    @DisplayName("Should Extract Filename From URL")
    void shouldExtractFilenameFromURL() {
        ProductImage productImage = new ProductImage("https://example.com/images/products/laptop.jpg");

        String filename = productImage.getFileName();

        assertEquals("laptop.jpg", filename);
    }

    @Test
    @DisplayName("Should Extract Filename From URL With Query Parameters")
    void shouldExtractFilenameFromURLWithQueryParameters() {
        ProductImage productImage = new ProductImage("https://example.com/images/product.jpg?version=1");

        String filename = productImage.getFileName();

        assertEquals("product.jpg?version=1", filename);
    }

    @Test
    @DisplayName("Should Return Full URL When Filename Cannot Be Extracted")
    void shouldReturnFullURLWhenFilenameCannotBeExtracted() {
        String urlWithoutPath = "https://example.com/";
        ProductImage productImage = new ProductImage(urlWithoutPath);

        String filename = productImage.getFileName();

        assertEquals(urlWithoutPath, filename);
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By URL")
    void shouldSupportEqualsAndHashCodeByURL() {
        ProductImage productImage1 = new ProductImage("https://example.com/image.jpg");
        ProductImage productImage2 = new ProductImage("https://example.com/image.jpg");

        assertEquals(productImage1, productImage2);
        assertEquals(productImage1.hashCode(), productImage2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When URLs Differ")
    void shouldNotBeEqualWhenURLsDiffer() {
        ProductImage productImage1 = new ProductImage("https://example.com/image1.jpg");
        ProductImage productImage2 = new ProductImage("https://example.com/image2.jpg");

        assertNotEquals(productImage1, productImage2);
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        ProductImage productImage = new ProductImage("https://example.com/image.jpg");

        assertNotNull(productImage.toString());
        assertFalse(productImage.toString().isEmpty());
        assertTrue(productImage.toString().contains("https://example.com/image.jpg"));
    }
}
