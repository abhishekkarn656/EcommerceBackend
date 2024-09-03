package com.example.ecommerce.Entity;

import com.example.ecommerce.Enum.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    private String email;

    private String name;

    private UserRole role;
    private String password;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

}
