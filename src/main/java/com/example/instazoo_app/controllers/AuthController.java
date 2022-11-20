package com.example.instazoo_app.controllers;

import com.example.instazoo_app.payload.response.MessageResponse;
import com.example.instazoo_app.payload.resquest.LoginRequest;
import com.example.instazoo_app.payload.resquest.SignupRequest;
import com.example.instazoo_app.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@PreAuthorize("permitAll()")
public class AuthController {

    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid
                                                   LoginRequest loginRequest,
                                                   BindingResult bindingResult){
        return authService.login(loginRequest, bindingResult);
    }
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid
                                                   SignupRequest signupRequest,
                                               BindingResult bindingResult){
        authService.registerUser(signupRequest, bindingResult);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
