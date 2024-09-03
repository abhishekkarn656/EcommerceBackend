package com.example.ecommerce.Services.auth;

import com.example.ecommerce.Dto.SignUpRequest;
import com.example.ecommerce.Dto.UserDto;
import com.example.ecommerce.Entity.User;
import com.example.ecommerce.Enum.UserRole;
import com.example.ecommerce.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AuthServiceImpl implements AuthService{

@Autowired
private UserRepository userRepository;
@Autowired
private BCryptPasswordEncoder bcrypptPasswordEncoder;

public UserDto createUser(SignUpRequest signUpRequest){
User user = new User();
user.setEmail(signUpRequest.getEmail());
user.setName(signUpRequest.getEmail());
user.setPassword( new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
user.setRole(UserRole.Customer);
User createdUser=userRepository.save(user);
UserDto userDto= new UserDto();
userDto.setId(createdUser.getId());
return  userDto;

}
public  boolean hasUserWithEmail(String email){
    Optional<User> user= userRepository.findFirstByEmail(email);
    if (user.isPresent()){
        return true;

    }
    else return false;

}
@PostConstruct
public void createAdminAccount(){
    User adminAccount =userRepository.findByRole(UserRole.ADMIN);
    if(null==adminAccount){
       User user= new User();
       user.setEmail("admin@gmail.com");
       user.setPassword( new BCryptPasswordEncoder().encode("admin"));
       user.setName("admin");
       user.setRole(UserRole.ADMIN);
       userRepository.save(user);
    }

}
}
