package com.debuggeandoideas.erp_lite.persistence.mongo.mappers;

import com.debuggeandoideas.erp_lite.domain.views.ProductView;
import com.debuggeandoideas.erp_lite.persistence.mongo.documents.ProductInCatalogDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProductCatalogMapper {

    @Mapping(source = "currency", target = "money")
    ProductView toView(ProductInCatalogDocument document);
}