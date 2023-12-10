package com.microservices.products.service;

import com.microservices.products.dto.ProductRequest;
import com.microservices.products.dto.ProductResponse;

import java.util.List;

public interface IProductSevice {

    void addProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();
}
