package com.newsfeed.auth.auth_service.service;

import com.newsfeed.auth.auth_service.dto.RegisterRequest;
import com.newsfeed.auth.auth_service.model.User;

public interface UserService {

    User register(RegisterRequest request);
}
