package com.debuggeandoideas.erp_lite.persistence.rest.dtos;

public record AddressDTO(
        String street,
        String suite,
        String city,
        String zipcode,
        GeoDTO geo
) {
}