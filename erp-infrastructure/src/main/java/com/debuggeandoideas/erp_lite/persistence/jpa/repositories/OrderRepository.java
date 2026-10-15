package com.debuggeandoideas.erp_lite.persistence.jpa.repositories;

import com.debuggeandoideas.erp_lite.persistence.jpa.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    List<OrderEntity> findAllByCustomerId(Long customerId);
}