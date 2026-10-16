package com.debuggeandoideas.erp_lite.persistence.mongo.repositories;

import com.debuggeandoideas.erp_lite.persistence.mongo.documents.ProductInCatalogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductInCatalogRepository extends MongoRepository<ProductInCatalogDocument, String> {

    Optional<ProductInCatalogDocument> findBySku(String id);

    List<ProductInCatalogDocument> findByNameContainingIgnoreCase(String text);

    @Query("{ '$text' :  { '$search' : ?0}, 'active' :  true}")
    List<ProductInCatalogDocument> findByTextAndActive(String text);

    List<ProductInCatalogDocument> findByCategoryIdAndActiveTrue(String category);

    List<ProductInCatalogDocument> findByActiveTrueOrderByIdAsc();
}