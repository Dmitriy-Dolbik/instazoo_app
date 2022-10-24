package com.example.instazoo_app.payload.resquest;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @NotEmpty(message = "Username cannot be empty")
    @Size(message = "Username must be between 2 to 100 characters")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
