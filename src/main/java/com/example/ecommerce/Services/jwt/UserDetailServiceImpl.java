package com.example.ecommerce.Services.jwt;

import com.example.ecommerce.Entity.User;
import com.example.ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailServiceImpl  implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findFirstByEmail(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("user is not found"+username);

        return  new org.springframework.security.core.userdetails.User(user.get().getEmail(),user.get().getPassword(), new ArrayList<>());
    }
}
