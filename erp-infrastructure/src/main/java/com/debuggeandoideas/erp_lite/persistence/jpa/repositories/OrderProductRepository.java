package com.debuggeandoideas.erp_lite.persistence.jpa.repositories;

import com.debuggeandoideas.erp_lite.persistence.jpa.entities.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, UUID> {
}