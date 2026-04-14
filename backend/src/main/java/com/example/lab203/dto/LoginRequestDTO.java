package com.example.lab203.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ???????
 */
@Data
public class LoginRequestDTO {

    @NotBlank(message = "???????")
    private String username;

    @NotBlank(message = "??????")
    private String password;
}
