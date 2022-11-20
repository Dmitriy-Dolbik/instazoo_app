package com.example.instazoo_app.services;

import com.example.instazoo_app.exceptions.NotFoundException;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findUserByEmail(username)
                .orElseThrow(()->new NotFoundException
                        ("User not found with username: " + username));
    }
    public User loadUserById(Long id){
        return userRepository.findUserById(id)
                .orElseThrow(()->new NotFoundException
                ("User not found with id: " + id));
    }
}
