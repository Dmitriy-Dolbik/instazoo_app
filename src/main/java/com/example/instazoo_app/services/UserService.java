package com.example.instazoo_app.services;

import com.example.instazoo_app.dto.UserDTO;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User updateUser(UserDTO userDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getName());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());
        return userRepository.save(user);
    }
    public User getCurrentUser(Principal principal){
        return getUserByPrincipal(principal);
    }
    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException(
                        "Username not found with username : "+username));
    }
    public Optional<User> findUserByEmail(String email) {

        return userRepository.findUserByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User cannot be found"));
    }
    public Optional<User> findUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }
}


