package com.example.instazoo_app.services;

import com.example.instazoo_app.exceptions.UserExistException;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.UsersRepository;
import com.example.instazoo_app.models.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    public static final Logger LOG = LoggerFactory.getLogger(RegistrationService.class);
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void register(User userIn){
        String encodedPassword = passwordEncoder.encode(userIn.getPassword());
        userIn.setPassword(encodedPassword);
        userIn.getRoles().add(Role.ROLE_USER);
        try {
            LOG.info("Saving User {}", userIn.getEmail());
            usersRepository.save(userIn);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user " + userIn.getUsername() + " already exist. Please check credentials");
        }
    }
}
