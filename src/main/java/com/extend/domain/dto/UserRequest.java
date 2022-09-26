package com.extend.domain.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Parameter(schema = @Schema(format = "password"))
    private String password;
}
