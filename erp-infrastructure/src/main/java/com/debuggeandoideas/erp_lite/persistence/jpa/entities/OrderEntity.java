package com.debuggeandoideas.erp_lite.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_orders_order_number",
                        columnNames = "order_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    @Column(name = "id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "order_number", length = 50, nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "customer_name", length = 200, nullable = false)
    private String customerName;

    @Column(name = "created_by", length = 100, nullable = false)
    private String createdBy;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderProductEntity> items = new ArrayList<>();

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (orderDate == null) {
            orderDate = now;
        }
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (status == null) {
            status = "PENDING";
        }
        if (currency == null) {
            currency = "USD";
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addItem(OrderProductEntity item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderProductEntity item) {
        items.remove(item);
        item.setOrder(null);
    }
}