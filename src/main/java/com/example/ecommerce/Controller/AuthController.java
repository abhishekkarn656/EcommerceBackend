package com.example.ecommerce.Controller;

import com.example.ecommerce.Dto.AuthenticationRequest;
import com.example.ecommerce.Dto.SignUpRequest;
import com.example.ecommerce.Dto.UserDto;
import com.example.ecommerce.Entity.User;
import com.example.ecommerce.Repository.UserRepository;
import com.example.ecommerce.Services.auth.AuthService;
import com.example.ecommerce.utils.jwtUtill;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final jwtUtill jwtUtill;
    private final AuthService authService;

    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_PREFIX = "Authorization";

//    @PostMapping("/authenticate")
//    public void createAuthenticationToken(@RequestBody AuthenticationRequest authRequest, HttpServletResponse response) throws IOException, JSONException {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        } catch (BadCredentialsException e) {
//            throw new BadCredentialsException("Username and password are incorrect");
//        }
//
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
//        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
//        final String jwt = jwtUtill.generateToken(userDetails.getUsername());
//        System.out.print(jwt);
//
//        if (optionalUser.isPresent()) {
//            System.out.print(optionalUser.isPresent());
//            response.getWriter().write(new JSONObject().put("userId", optionalUser.get().getId())
//                    .put("role", optionalUser.get().getRole())
//                    .toString()
//            );
//            response.addHeader("Access-Control-Expose-Header","Authorization");
//            response.addHeader("Access-Control-Allow-Header","Authorization,X-PINGOTHER,ORIGIN"+"X-Requested-with,Content-Type,Accept,X-Custom-header");
//            response.addHeader(HEADER_PREFIX, TOKEN_PREFIX + " " + jwt);
//        }
//    }
@PostMapping("/authenticate")
public void createAuthenticationToken(@RequestBody AuthenticationRequest authRequest, HttpServletResponse response) throws IOException, JSONException {
    try {
        // Authenticate the user with the provided username and password
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
    } catch (BadCredentialsException e) {
        throw new BadCredentialsException("Username and password are incorrect");
    }

    // Load user details
    final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
    Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

    // Generate JWT token
    final String jwt = jwtUtill.generateToken(userDetails.getUsername());
    System.out.println(jwt);

    if (optionalUser.isPresent()) {
        // Prepare JSON response with user ID and role
        String jsonResponse = new JSONObject()
                .put("userId", optionalUser.get().getId())
                .put("role", optionalUser.get().getRole())
                .toString();

        // Write JSON response
        response.getWriter().write(jsonResponse);

        // Add CORS headers
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-Header");

        // Add the Authorization header with the JWT token
        response.setHeader("Authorization", "Bearer " + jwt);
    }
}

    @PostMapping("/SIGN-UP")
    public ResponseEntity<?> signupUser(@RequestBody SignUpRequest signUpRequest) {
        if (authService.hasUserWithEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto userDto = authService.createUser(signUpRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
