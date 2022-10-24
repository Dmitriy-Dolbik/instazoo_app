package com.example.instazoo_app.services;

import com.example.instazoo_app.exceptions.UserExistException;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.models.enums.Role;
import com.example.instazoo_app.payload.resquest.SignupRequest;
import com.example.instazoo_app.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //переписать сохранение человека через userDto, Mapper и Handler в контроллере
    //смотри Alishev
    public User creatUser(SignupRequest userIn){
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        try{
            LOG.info("Saving User {}", userIn.getEmail());
            return usersRepository.save(user);
        } catch (Exception e){
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user "+user.getUsername()+" alreadyExist. Please check credentials");

        }

    }


}
