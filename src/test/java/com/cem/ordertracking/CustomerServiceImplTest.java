package com.cem.ordertracking;

import com.cem.ordertracking.exception.ResourceNotFoundException;
import com.cem.ordertracking.repository.CustomerRepository;
import com.cem.ordertracking.security.Customer;
import com.cem.ordertracking.security.Role;
import com.cem.ordertracking.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService; // The service class to be tested


    @Test
    public void testFindCustomerById_success() {

        Customer mockCustomer = new Customer(1L,"admin","adminpass","admin","admin","admin","admin", Role.ADMIN);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

        Customer foundCustomer = customerService.findCustomerById(1L);

        assertNotNull(foundCustomer);
        assertEquals("admin", foundCustomer.getUsername());
        assertEquals("adminpass", foundCustomer.getPassword());
    }
    @Test
    public void testFindCustomerById_failure() {


        Long customerId = 456L;  // Non-existent product ID
        assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findCustomerById(customerId);
        });

    }

    @Test
    public void testSaveCustomer_success() {

        Customer customer = new Customer(1L,"admin","adminpass","admin","admin","admin","admin", Role.ADMIN);


        when(customerRepository.save(any(Customer.class))).thenReturn(customer);


        Customer savedCustomer = customerService.saveCustomer(customer);


        verify(customerRepository).save(customer);


        assertEquals("admin", savedCustomer.getEmail());
        assertEquals("adminpass", savedCustomer.getPassword());


    }

    @Test
    public void testSaveCustomer_failure() {

        Customer customer = new Customer(1L,"admin","adminpass","admin","admin","admin","admin", Role.ADMIN);


        when(customerRepository.save(any(Customer.class))).thenThrow(RuntimeException.class);


        assertThrows(RuntimeException.class, () -> customerService.saveCustomer(customer));


        verify(customerRepository).save(customer);
    }



    @Test
    public void testUpdateCustomer_Success() {

        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Customer updatedCustomer = new Customer(1L,"admin","adminpass","admin","admin","admin","admin", Role.ADMIN);



        Customer result = customerService.saveCustomer(updatedCustomer);


        verify(customerRepository).save(updatedCustomer);


    }



}
