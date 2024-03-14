package com.cem.ordertracking.controller;

import com.cem.ordertracking.entity.Product;
import com.cem.ordertracking.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;



    @GetMapping("/products")
    public List<Product> findAllProducts(){
        return productService.findAllProducts();
    }

    @GetMapping("/products/{productId}")
    public Product findProduct(@PathVariable long productId){

        return productService.findProductById(productId);
    }

    @PostMapping("/products")
    public void addProduct(@RequestBody Product product){

        product.setId(0L);
        productService.saveProduct(product);

    }

    @PutMapping("/products")
    public void updateProduct(@RequestBody Product product){
        productService.saveProduct(product);
    }

    @DeleteMapping("/products/{productId}")
    public void deleteProduct(@PathVariable long productId){

        productService.deleteProduct(productId);

    }





}
