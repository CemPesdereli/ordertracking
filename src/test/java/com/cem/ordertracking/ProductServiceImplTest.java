package com.cem.ordertracking;

import com.cem.ordertracking.entity.Product;
import com.cem.ordertracking.exception.ResourceNotFoundException;
import com.cem.ordertracking.repository.ProductRepository;
import com.cem.ordertracking.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService; // The service class to be tested


    @Test
    public void testFindProductById_success() {
        BigDecimal productPrice = new BigDecimal("27.50");
        Product mockProduct = new Product("Mouse", "Gaming Mouse", "Electronic",productPrice,100);
        mockProduct.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        Product foundProduct = productService.findProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Mouse", foundProduct.getName());
        assertEquals("Electronic", foundProduct.getCategory());
    }

    @Test
    public void testFindProductById_failure(){
        Long productId = 456L;  // Non-existent product ID

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findProductById(productId);
        });


    }

    @Test
    public void testSaveProduct_success() {
        // Create a sample product
        Product product = new Product("Sample Product", new BigDecimal("10.99"));

        // Mock the behavior of ProductRepository save method
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Call the saveProduct method of ProductService
        Product savedProduct = productService.saveProduct(product);

        // Verify that the save method of ProductRepository is called with the correct parameter
        verify(productRepository).save(product);

        // Verify the returned product
        assertEquals("Sample Product", savedProduct.getName());
        assertEquals(new BigDecimal("10.99"), savedProduct.getPrice());


    }

    @Test
    public void testSaveProduct_failure() {
        // Create a sample product
        Product product = new Product("Sample Product", new BigDecimal("10.99"));

        // Mock the behavior of ProductRepository save method to throw an exception
        when(productRepository.save(any(Product.class))).thenThrow(RuntimeException.class);

        // Verify that the ProductService saveProduct method throws an exception when the save operation fails
        assertThrows(RuntimeException.class, () -> productService.saveProduct(product));

        // Verify that the save method of ProductRepository is called with the correct parameter
        verify(productRepository).save(product);
    }
    @Test
    public void testDeleteProduct() {
        // Define the ID of the product to be deleted
        long productId = 1L;

        // Mock the behavior of ProductRepository deleteById method
        // Since deleteById method returns void, we just mock the method call
        when(productRepository.existsById(productId)).thenReturn(true); // Assume product exists

        // Call the deleteProduct method of ProductService
        productService.deleteProduct(productId);

        // Verify that the deleteById method of ProductRepository is called with the correct parameter
        verify(productRepository).deleteById(productId);


    }

    @Test
    public void testDeleteProduct_Failure_ProductNotFound() {
        long productId = 1L;

        // Mock the behavior of ProductRepository existsById method to return false, indicating the product doesn't exist
        when(productRepository.existsById(productId)).thenReturn(false);

        // Call the deleteProduct method of ProductService
        productService.deleteProduct(productId);

        // No need to verify the deleteById method of ProductRepository in this case
    }

    @Test
    public void testUpdateProduct_Success() {
        // Mock the behavior of ProductRepository save method
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Create a sample product to update
        Product updatedProduct = new Product("Updated Product", BigDecimal.valueOf(25.0));
        updatedProduct.setId(1L);

        // Call the updateProduct method of ProductService
        Product result = productService.saveProduct(updatedProduct);

        // Verify that the save method of ProductRepository is called with the correct parameter
        verify(productRepository).save(updatedProduct);


    }


}
