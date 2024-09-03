package com.example.ecommerce.Dto;

import com.example.ecommerce.Enum.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String email;
    private String name;

    private UserRole userRole;
}
