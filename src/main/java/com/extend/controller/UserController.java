package com.extend.controller;

import com.extend.domain.dto.UserRequest;
import com.extend.domain.dto.UserResponse;
import com.extend.domain.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Tag(name = "Users")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.login(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout(@RequestParam @NotBlank String refreshToken) {
        userService.logout(refreshToken);
    }

    @PostMapping(value = "/renewauth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> refreshToken(@RequestParam @NotBlank String refreshToken) {
        return new ResponseEntity<>(userService.refreshToken(refreshToken), HttpStatus.OK);
    }
}
