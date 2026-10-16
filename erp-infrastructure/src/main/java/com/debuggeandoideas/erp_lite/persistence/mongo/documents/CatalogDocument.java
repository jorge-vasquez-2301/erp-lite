package com.debuggeandoideas.erp_lite.persistence.mongo.documents;

import com.debuggeandoideas.erp_lite.enums.CatalogType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "catalogs")
public class CatalogDocument {

    @Id
    private String id;

    @Field(name = "active")
    private boolean active;

    private CatalogType catalogType;

    private Instant createdAt;

    private Instant updatedAt;

    private String description;

    private String name;

    private List<CatalogItem> items;
}