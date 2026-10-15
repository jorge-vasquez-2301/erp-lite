package com.debuggeandoideas.erp_lite.domain.ports.services;

import com.debuggeandoideas.erp_lite.domain.entities.product.ProductImage;

public interface ImageStorageServicePort {

    ProductImage upload(String imageName, byte[] imageData);

    void delete(ProductImage img);

    byte[] download(ProductImage img);
}