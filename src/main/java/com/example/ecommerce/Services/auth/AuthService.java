package com.example.ecommerce.Services.auth;

import com.example.ecommerce.Dto.SignUpRequest;
import com.example.ecommerce.Dto.UserDto;

public interface AuthService {
    public UserDto createUser(SignUpRequest signUpRequest);
    public  boolean hasUserWithEmail(String email);
}
