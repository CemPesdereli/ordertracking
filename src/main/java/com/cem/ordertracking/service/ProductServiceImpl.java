package com.cem.ordertracking.service;

import com.cem.ordertracking.entity.Product;
import com.cem.ordertracking.exception.ResourceNotFoundException;
import com.cem.ordertracking.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{


    private final ProductRepository productRepository;




    @Override
    @Transactional
    public Product saveProduct(Product product) {
        System.out.println("Product is saving");
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            // Handle other unexpected exceptions (e.g., log error)
            throw new RuntimeException("Failed to save product: " + e.getMessage());
        }

    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(Long id) {

        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            System.out.println("Product deleted with id: "+id);
        } catch (Exception  e) {
            // Handle constraint violation (e.g., log error, throw custom exception)
            throw new RuntimeException("Failed to delete product: " + e.getMessage());
        }

    }
}
