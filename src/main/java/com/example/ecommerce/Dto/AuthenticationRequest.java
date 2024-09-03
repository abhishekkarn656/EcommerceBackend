package com.example.ecommerce.Dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
