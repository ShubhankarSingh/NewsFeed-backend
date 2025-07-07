package com.newsfeed.auth.auth_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@Builder
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private List<String> roles;
}