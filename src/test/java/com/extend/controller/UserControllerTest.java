package com.extend.controller;

import com.extend.config.Constants;
import com.extend.domain.dto.UserResponse;
import com.extend.infrastructure.client.ExtendClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@Import({ RestExceptionHandler.class })
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExtendClient extendClient;

    @Test
    public void login_givenValidUserRequest_shouldReturnResponseWithToken() throws Exception {
        when(extendClient.login(any())).thenReturn(
                new UserResponse("abc123DEF456ghi789JKL012", "abc123DEF456ghi789JKL012"));
        mockMvc.perform(
                post(Constants.LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"demo@extend.com\", \"password\": \"demo123\" }\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("abc123DEF456ghi789JKL012"));
        verify(extendClient, times(1)).login(any());
    }

    @Test
    public void login_givenInvalidRequest_shouldReturnStatus401() throws Exception {
        when(extendClient.login(any())).thenThrow(HttpClientErrorException.Unauthorized.class);
        mockMvc.perform(
                        post(Constants.LOGIN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"email\": \"demo@extend.com\", \"password\": \"demo\" }\""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("invalid login request"));
        verify(extendClient, times(1)).login(any());
    }

    @Test
    public void login_givenInvalidEmail_shouldReturnStatus404() throws Exception {
        mockMvc.perform(
                        post(Constants.LOGIN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"email\": \"demo\", \"password\": \"demo\" }\""))
                .andExpect(status().isBadRequest());
        verify(extendClient, times(0)).login(any());
    }

    @Test
    public void login_givenPasswordIsNull_shouldReturnStatus404() throws Exception {
        mockMvc.perform(
                        post(Constants.LOGIN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"email\": \"demo@extend.com\", \"password\": \"\" }\""))
                .andExpect(status().isBadRequest());
    }
}