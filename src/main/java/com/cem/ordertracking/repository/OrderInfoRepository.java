package com.cem.ordertracking.repository;

import com.cem.ordertracking.entity.OrderInfo;
import com.cem.ordertracking.entity.OrderItem;
import com.cem.ordertracking.security.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderInfoRepository extends JpaRepository<OrderInfo,Long> {


    List<OrderInfo> findByCustomer(Customer customer);


}
