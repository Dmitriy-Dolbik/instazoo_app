package com.example.instazoo_app.payload.resquest;

import com.example.instazoo_app.annotations.PasswordMatches;
import com.example.instazoo_app.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {
    @NotEmpty(message = "Please enter your name")
    private String name;
    @NotEmpty(message = "Please enter your username")
    private String username;
    @NotEmpty(message = "Please enter your lastname")
    private String lastname;
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must contain more than 6 characters")
    private String password;
    private String confirmPassword;
}
