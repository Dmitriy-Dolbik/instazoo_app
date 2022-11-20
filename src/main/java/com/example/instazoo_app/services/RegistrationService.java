package com.example.instazoo_app.services;

import com.example.instazoo_app.exceptions.InvalidRequestValuesException;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.UserRepository;
import com.example.instazoo_app.models.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(User userIn) {
        String encodedPassword = passwordEncoder.encode(userIn.getPassword());
        userIn.setPassword(encodedPassword);
        userIn.getRoles().add(Role.ROLE_USER);
        try {
            log.info("Saving User {}", userIn.getEmail());
            userRepository.save(userIn);
        } catch (Exception e) {
            log.error("Error during registration. {}", e.getMessage());
            throw new InvalidRequestValuesException("The user " + userIn.getUsername() + " already exist. Please check credentials");
        }
    }
}
