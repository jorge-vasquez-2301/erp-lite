package com.debuggeandoideas.erp_lite.persistence.mongo.documents.repositories;

import com.debuggeandoideas.erp_lite.persistence.mongo.documents.ProductInCatalogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductInCatalogRepository extends MongoRepository<ProductInCatalogDocument, String> {
}