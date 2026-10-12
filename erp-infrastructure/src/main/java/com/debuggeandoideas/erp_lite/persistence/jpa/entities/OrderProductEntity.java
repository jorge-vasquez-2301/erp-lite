package com.debuggeandoideas.erp_lite.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "order_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductEntity {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    @Column(name = "id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_products_order")
    )
    private OrderEntity order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_products_product")
    )
    private ProductEntity product;

    @Column(name = "product_name", length = 200, nullable = false)
    private String productName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "subtotal", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @PrePersist
    void prePersist() {
        if (unitPrice != null && quantity != null && subtotal == null) {
            subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity.longValue()));
        }
        if (product != null && (productName == null || productName.isBlank())) {
            productName = product.getName();
        }
    }
}