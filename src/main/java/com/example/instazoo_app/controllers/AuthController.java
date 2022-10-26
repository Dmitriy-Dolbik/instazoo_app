package com.example.instazoo_app.controllers;

import com.example.instazoo_app.payload.resquest.SignupRequest;
import com.example.instazoo_app.exceptions.AuthException;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.payload.response.AuthErrorResponse;
import com.example.instazoo_app.payload.response.JWTTokenSuccessResponse;
import com.example.instazoo_app.payload.response.MessageResponse;
import com.example.instazoo_app.payload.resquest.LoginRequest;
import com.example.instazoo_app.security.JWTTokenProvider;
import com.example.instazoo_app.security.SecurityConstants;
import com.example.instazoo_app.services.RegistrationService;
import com.example.instazoo_app.validations.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.example.instazoo_app.util.ErrorUtil.returnErrorsToClient;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@PreAuthorize("permitAll()")
public class AuthController {
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;
    @Autowired
    public AuthController(JWTTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserValidator userValidator, RegistrationService registrationService, ModelMapper modelMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody @Valid
                                                   LoginRequest loginRequest,
                                                   BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            returnErrorsToClient(bindingResult);
        }
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword());
        try{
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = SecurityConstants.TOKEN_PREFIX+jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
        }catch(BadCredentialsException exc){
            throw new AuthException("Incorrect credentials");
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid
                                                   SignupRequest signupRequest,
                                               BindingResult bindingResult){
        User user = convertToUser(signupRequest);
        userValidator.validate(user,bindingResult);
        if (bindingResult.hasErrors()){
            returnErrorsToClient(bindingResult);
        }
        registrationService.register(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    private User convertToUser(SignupRequest signupRequest){
        return modelMapper.map(signupRequest, User.class);
    }
    @ExceptionHandler
    private ResponseEntity<AuthErrorResponse> handleException(@NotNull final AuthException exc){
        AuthErrorResponse response = new AuthErrorResponse(exc.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}
