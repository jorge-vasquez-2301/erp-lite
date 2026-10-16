package com.debuggeandoideas.erp_lite.persistence.mongo.documents;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Document(collection = "product_documents")
public class ProductInCatalogDocument {

    @Id
    private String id;

    private boolean active;

    private String categoryId;

    private String categoryName;

    private Instant createdAt;

    private Instant updatedAt;

    private String currency;

    private String description;

    private String imageUrl;

    private String name;

    private BigDecimal price;

    private String sku;

    private Map<String, Object> specifications;

    private int stock;

    private List<String> tags;

}