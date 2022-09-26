package com.extend.domain.service;

import com.extend.domain.dto.UserRequest;
import com.extend.domain.dto.UserResponse;

public interface UserService {
    UserResponse login(UserRequest userRequest);
    void logout(String refreshToken);

    UserResponse refreshToken(String refreshToken);
}
