package com.example.instazoo_app.services;

import com.example.instazoo_app.dto.UserDTO;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    private final UsersRepository usersRepository;
    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    public User updateUser(User userForUpdate, Principal principal){
        User user = getUserByPrincipal(principal);
        user.setName(userForUpdate.getName());
        user.setLastname(userForUpdate.getLastname());
        user.setBio(userForUpdate.getBio());
        return usersRepository.save(user);
    }
    public User getCurrentUser(Principal principal){
        return getUserByPrincipal(principal);
    }
    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return usersRepository.findUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(
                        "Username not found with username : "+username));
    }
    public Optional<User> findUserByEmail(String email) {
        return usersRepository.findUserByEmail(email);
    }

    public User getUserById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User cannot be found"));
    }
}


