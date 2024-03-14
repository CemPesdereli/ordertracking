package com.cem.ordertracking.service;

import com.cem.ordertracking.entity.Product;

import java.util.List;

public interface ProductService {


    Product saveProduct(Product product);

    List<Product> findAllProducts();

    Product findProductById(Long id);

    void deleteProduct(Long id);


}
