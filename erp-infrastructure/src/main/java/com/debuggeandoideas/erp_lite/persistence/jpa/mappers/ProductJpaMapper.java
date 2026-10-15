package com.debuggeandoideas.erp_lite.persistence.jpa.mappers;

import com.debuggeandoideas.erp_lite.domain.entities.product.*;
import com.debuggeandoideas.erp_lite.domain.shared.AuditInfo;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import com.debuggeandoideas.erp_lite.persistence.jpa.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Currency;

/**
 * Anti-Corruption Layer between ProductRoot (Domain) and ProductEntity (JPA).
 * Handles value object unwrapping/wrapping and Instant ←→ LocalDateTime conversion.
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProductJpaMapper {

    // ── Domain → Entity ─────────────────────────────────────────────────
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "sku.value", target = "sku")
    @Mapping(source = "name.value", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price.amount", target = "price")
    @Mapping(source = "stock.value", target = "stock")
    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(source = "image.imageUrl", target = "imageUrl")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "auditInfo.createdAt", target = "createdAt", qualifiedByName = "instantToLocalDateTime")
    @Mapping(source = "auditInfo.updatedAt", target = "updatedAt", qualifiedByName = "instantToLocalDateTime")
    ProductEntity toEntity(ProductRoot domain);

    // ── Entity → Domain ─────────────────────────────────────────────────

    /**
     * Reconstitutes a ProductRoot from a JPA entity.
     * Uses reflection to access the private constructor since domain aggregates
     * enforce encapsulated construction via factory methods.
     */
    default ProductRoot toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        try {
            var constructor = ProductRoot.class.getDeclaredConstructor(
                    ProductId.class, SKU.class, ProductName.class, String.class,
                    Money.class, Stock.class, CategoryReference.class, ProductImage.class,
                    boolean.class, AuditInfo.class
            );
            constructor.setAccessible(true);

            return constructor.newInstance(
                    ProductId.of(entity.getId()),
                    SKU.of(entity.getSku()),
                    ProductName.of(entity.getName()),
                    entity.getDescription(),
                    Money.of(entity.getPrice(), Currency.getInstance("USD")),
                    Stock.of(entity.getStock()),
                    entity.getCategoryId() != null ? CategoryReference.of(entity.getCategoryId()) : null,
                    entity.getImageUrl() != null ? ProductImage.of(entity.getImageUrl()) : null,
                    entity.isActive(),
                    new AuditInfo(
                            "system",
                            entity.getCreatedAt().toInstant(ZoneOffset.UTC),
                            entity.getUpdatedAt().toInstant(ZoneOffset.UTC)
                    )
            );
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to reconstitute ProductRoot from ProductEntity", e);
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    /**
     * Converts Instant (domain) to LocalDateTime (JPA) using UTC zone.
     */
    @Named("instantToLocalDateTime")
    default LocalDateTime instantToLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}