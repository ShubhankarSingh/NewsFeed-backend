package com.newsfeed.auth.auth_service.service.impl;

import com.newsfeed.auth.auth_service.config.JWTService;
import com.newsfeed.auth.auth_service.dto.LoginRequest;
import com.newsfeed.auth.auth_service.dto.RegisterRequest;
import com.newsfeed.auth.auth_service.exception.InvalidCredentialsException;
import com.newsfeed.auth.auth_service.exception.UserAlreadyExistsException;
import com.newsfeed.auth.auth_service.model.User;
import com.newsfeed.auth.auth_service.repository.UserRepository;
import com.newsfeed.auth.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public User register(RegisterRequest request) {

        Optional<User> theUser = userRepository.findByEmail(request.getEmail());

        if(theUser.isPresent()){
            throw new UserAlreadyExistsException("User with email already exists: " + request.getEmail());
        }

        String email = request.getEmail();
        String extractedUsername = email.substring(0, email.indexOf("@"));

        User user = User.builder()
                .username(extractedUsername)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        return userRepository.save(user);
    }

    @Override
    public String authenticate(LoginRequest request) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            System.out.println("User details " + user.getUsername());
            return jwtService.generateToken(user.getUsername());

        }catch (BadCredentialsException ex){
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }
}
