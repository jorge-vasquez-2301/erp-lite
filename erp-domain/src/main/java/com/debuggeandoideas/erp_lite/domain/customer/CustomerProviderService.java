package com.debuggeandoideas.erp_lite.domain.customer;

import java.util.Optional;

/**
 * Port for external service for JSONPlaceholder
 */
public interface CustomerProviderService {

    Optional<CustomerInfo> findById(Long id);

    boolean existsById(Long id);
}
