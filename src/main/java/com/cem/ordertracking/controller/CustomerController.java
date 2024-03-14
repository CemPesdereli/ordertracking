package com.cem.ordertracking.controller;

import com.cem.ordertracking.auth.AuthenticationResponse;
import com.cem.ordertracking.auth.AuthenticationService;
import com.cem.ordertracking.auth.RegisterRequest;
import com.cem.ordertracking.config.JwtService;
import com.cem.ordertracking.security.Customer;
import com.cem.ordertracking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationService service;
    private final JwtService jwtService;



    @GetMapping("/customers")
    public List<Customer> findAllCustomers(){
        return customerService.findAllCustomers();
    }

    @GetMapping("/customers/{customerId}")
    public Customer findCustomer(@PathVariable long customerId){

        return customerService.findCustomerById(customerId);
    }

    @PostMapping("/customers")
    public void addCustomer(@RequestBody Customer customer){

        customer.setId(0L);
        customerService.saveCustomer(customer);

    }

    @PutMapping("/customers")
    public ResponseEntity<AuthenticationResponse> updateCustomer(
            @RequestHeader("Authorization") String token,
            @RequestBody RegisterRequest request
    ){
        String jwt = token.substring(7);
        String username= jwtService.extractUsername(jwt);
        Customer tempcustomer= customerService.findByUsername(username);
        request.setId(tempcustomer.getId());
        return ResponseEntity.ok(service.updateCustomer(request));
    }

    @DeleteMapping("/customers/{customerId}")
    public void deleteCustomer(@PathVariable long customerId){

        customerService.deleteCustomer(customerId);

    }




}
