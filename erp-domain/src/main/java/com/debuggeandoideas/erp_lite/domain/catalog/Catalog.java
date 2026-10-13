package com.debuggeandoideas.erp_lite.domain.catalog;

import com.debuggeandoideas.erp_lite.domain.common.AggregateRoot;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Catalog extends AggregateRoot<String> {

    private final CatalogType catalogType;
    private final String name;
    private final String description;
    private final List<CatalogItem> items;
    private final boolean isActive;

    public Catalog(
            String id,
            CatalogType catalogType,
            String name,
            String description,
            List<CatalogItem> items,
            boolean isActive) {

        super(id);

        if (catalogType == null) {
            throw new IllegalArgumentException("catalogType cannot be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        this.catalogType = catalogType;
        this.name = name;
        this.description = description;
        this.items = items;
        this.isActive = isActive;
    }

    public Optional<CatalogItem> findItemByCode(String code) {
        return this.items
                .stream()
                .filter(item -> item.getCode().equals(code))
                .findFirst();
    }

    public boolean containsItem(String code) {
        return this.findItemByCode(code).isPresent();
    }

    public List<CatalogItem> findItemsActive() {
        return this.items.stream()
                .filter(CatalogItem::isActive)
                .toList();
    }

    public List<CatalogItem> findAll() {
        return Collections.unmodifiableList(this.items);
    }
}
