package com.cem.ordertracking.service;

import com.cem.ordertracking.entity.OrderInfo;
import com.cem.ordertracking.entity.OrderItem;
import com.cem.ordertracking.entity.Product;
import com.cem.ordertracking.repository.OrderInfoRepository;
import com.cem.ordertracking.repository.OrderItemRepository;
import com.cem.ordertracking.repository.ProductRepository;
import com.cem.ordertracking.security.Customer;
import com.cem.ordertracking.repository.CustomerRepository;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;



    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        //customer.setPassword(passwordEncoder.encode(customer.getPassword()));  // Encode password
        System.out.println("Saving customer "+customer);
        return customerRepository.save(customer);
    }


    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow();

        List<OrderInfo> orderInfos = orderInfoRepository.findByCustomer(customer);

        for (OrderInfo orderInfo : orderInfos) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderInfo(orderInfo);
            for (OrderItem orderItem : orderItems) {
                Product product = orderItem.getProduct();
                product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
                productRepository.save(product);
            }
            orderItemRepository.deleteAll(orderItems);
        }

        orderInfoRepository.deleteAll(orderInfos);




        customerRepository.deleteById(id);
        System.out.println("Customer deleted with id: "+id);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }


}
