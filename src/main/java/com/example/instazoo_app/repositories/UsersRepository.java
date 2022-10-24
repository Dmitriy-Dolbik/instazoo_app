package com.example.instazoo_app.repositories;

import com.example.instazoo_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById(Long id);
}
