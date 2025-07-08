package com.newsfeed.auth.auth_service.service.impl;

import com.newsfeed.auth.auth_service.config.JWTService;
import com.newsfeed.auth.auth_service.dto.LoginRequest;
import com.newsfeed.auth.auth_service.dto.RegisterRequest;
import com.newsfeed.auth.auth_service.model.User;
import com.newsfeed.auth.auth_service.repository.UserRepository;
import com.newsfeed.auth.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private JWTService jwtService;

    @Override
    public User register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        return userRepository.save(user);
    }

    @Override
    public String authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));
        return jwtService.generateToken(user.getEmail());
    }
}
