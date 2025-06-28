package com.newsfeed.auth.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private List<String> roles;
}