package com.cem.ordertracking.auth;


import com.cem.ordertracking.auth.AuthenticationRequest;
import com.cem.ordertracking.auth.AuthenticationResponse;
import com.cem.ordertracking.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
            ){
            return ResponseEntity.ok(service.register(request));
    }

    @PutMapping("/register")
    public ResponseEntity<AuthenticationResponse> updateCustomer(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.updateCustomer(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }



}
