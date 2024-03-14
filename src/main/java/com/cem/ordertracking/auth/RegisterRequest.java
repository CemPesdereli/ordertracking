package com.cem.ordertracking.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Long id;
    private String firstname;
    private String lastname;

    private String username;
    private String password;
    private String email;
    private String shippingAddress;
}
