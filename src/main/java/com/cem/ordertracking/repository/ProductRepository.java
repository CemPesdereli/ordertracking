package com.cem.ordertracking.repository;

import com.cem.ordertracking.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
