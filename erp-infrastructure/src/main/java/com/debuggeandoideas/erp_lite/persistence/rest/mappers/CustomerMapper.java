package com.debuggeandoideas.erp_lite.persistence.rest.mappers;

import com.debuggeandoideas.erp_lite.domain.customer.CustomerInfo;
import com.debuggeandoideas.erp_lite.persistence.rest.dtos.AddressDTO;
import com.debuggeandoideas.erp_lite.persistence.rest.dtos.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * Anti-Corruption Layer between external API (JSONPlaceholder) and domain
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CustomerMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")

    @Mapping(source = "address", target = "address", qualifiedByName = "mapAddress")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.zipcode", target = "zipcode")

    @Mapping(source = "company.name", target = "companyName")
    CustomerInfo toCustomerInfo(UserDTO userDTO);

    /**
     * Combines street + suite into a single string, null/blank-safe
     */
    @Named("mapAddress")
    default String mapAddress(AddressDTO address) {
        if (address == null) {
            return null;
        }

        String street = address.street();
        String suite = address.suite();

        // Prefer clean output: avoid "street, null" or "street, "
        if (suite == null || suite.isBlank()) {
            return street;
        }
        if (street == null || street.isBlank()) {
            return suite;
        }

        return street + ", " + suite;
    }
}