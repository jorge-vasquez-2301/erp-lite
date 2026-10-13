package com.debuggeandoideas.erp_lite.domain.ports;

import com.debuggeandoideas.erp_lite.domain.product.ProductImage;

public interface ImageStorageService {

    ProductImage upload(String imageName, byte[] imageData);

    void delete(ProductImage img);

    byte[] download(ProductImage img);
}