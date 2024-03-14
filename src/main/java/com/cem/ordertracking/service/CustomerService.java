package com.cem.ordertracking.service;

import com.cem.ordertracking.security.Customer;


import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer saveCustomer(Customer customer);

    List<Customer> findAllCustomers();

    Customer findCustomerById(Long id);

    void deleteCustomer(Long id);

    Customer findByUsername(String username);
}
