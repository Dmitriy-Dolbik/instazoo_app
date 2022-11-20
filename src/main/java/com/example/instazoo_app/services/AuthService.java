package com.example.instazoo_app.services;

import com.example.instazoo_app.exceptions.AuthException;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.payload.response.JWTTokenSuccessResponse;
import com.example.instazoo_app.payload.resquest.LoginRequest;
import com.example.instazoo_app.payload.resquest.SignupRequest;
import com.example.instazoo_app.security.JWTTokenProvider;
import com.example.instazoo_app.security.SecurityConstants;
import com.example.instazoo_app.validations.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import static com.example.instazoo_app.util.ErrorUtil.createErrorMessageToClient;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    public ResponseEntity<Object> login(LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = createErrorMessageToClient(bindingResult);
            throw new AuthException(errorMsg);
        }
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
        } catch (BadCredentialsException exc) {
            throw new AuthException("Incorrect credentials");
        }
    }

    public void registerUser(SignupRequest signupRequest, BindingResult bindingResult) {
        User user = modelMapper.map(signupRequest, User.class);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMsg = createErrorMessageToClient(bindingResult);
            throw new AuthException(errorMsg);
        }
        registrationService.register(user);
    }
}
