package com.debuggeandoideas.erp_lite.persistence.mongo.mappers;

import com.debuggeandoideas.erp_lite.domain.views.CatalogView;
import com.debuggeandoideas.erp_lite.domain.views.ItemsView;
import com.debuggeandoideas.erp_lite.persistence.mongo.documents.CatalogDocument;
import com.debuggeandoideas.erp_lite.persistence.mongo.documents.CatalogItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CatalogMapper {

    @Mapping(source = "catalogType", target = "type")
    CatalogView toView(CatalogDocument document);

    ItemsView toItemView(CatalogItem item);
}