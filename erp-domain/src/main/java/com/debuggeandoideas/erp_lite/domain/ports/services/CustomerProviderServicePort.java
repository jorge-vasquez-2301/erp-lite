package com.debuggeandoideas.erp_lite.domain.ports.services;

import com.debuggeandoideas.erp_lite.domain.entities.customer.CustomerInfo;

import java.util.Optional;

/**
 * Port for external service for JSONPlaceholder
 */
public interface CustomerProviderServicePort {

    Optional<CustomerInfo> findById(Long id);

    boolean existsById(Long id);
}
