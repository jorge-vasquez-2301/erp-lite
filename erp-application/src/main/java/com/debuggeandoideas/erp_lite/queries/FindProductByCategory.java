package com.debuggeandoideas.erp_lite.queries;

import com.debuggeandoideas.erp_lite.domain.ports.repositories.ProductCatalogRepositoryPort;
import com.debuggeandoideas.erp_lite.domain.views.ProductView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindProductByCategory {

    private final ProductCatalogRepositoryPort productCatalogRepository;


    public List<ProductView> execute(String category) {
        log.info("Execute FindProductByCategory");

        return productCatalogRepository.findByCategory(category);
    }
}