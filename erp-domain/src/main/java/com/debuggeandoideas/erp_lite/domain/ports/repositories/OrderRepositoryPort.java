package com.debuggeandoideas.erp_lite.domain.ports.repositories;

import com.debuggeandoideas.erp_lite.domain.entities.order.OrderId;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderNumber;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderRoot;
import com.debuggeandoideas.erp_lite.domain.shared.CustomerId;

import java.util.List;
import java.util.Optional;


/**
 * Port for storage o consult Orders
 */
public interface OrderRepositoryPort {
    OrderRoot save(OrderRoot orderRoot);

    Optional<OrderRoot> findById(OrderId id);

    Optional<OrderRoot> findAllByOrderNumber(OrderNumber orderNumber);

    List<OrderRoot> findByCustomerId(CustomerId customerId);

    void delete(OrderRoot orderRoot);

}