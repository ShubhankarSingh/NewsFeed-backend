package com.newsfeed.auth.auth_service.controller;

import com.newsfeed.auth.auth_service.dto.RegisterRequest;
import com.newsfeed.auth.auth_service.model.User;
import com.newsfeed.auth.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request){

        User registeredUser = userService.register(request);

        return ResponseEntity.ok(registeredUser);

    }
}
