package com.example.instazoo_app.services;

import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = usersRepository.findUserByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException
                        ("User not found with username: " + username));
        return build(user);
    }
    public User loadUserById(Long id){
        return usersRepository.findUserById(id).orElse(null);
    }
    public static User build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role->new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}