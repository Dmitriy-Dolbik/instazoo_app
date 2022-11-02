package com.example.instazoo_app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTTokenProvider {
    @Value("${jwt_secret}")
    private String secret;
    private final UsersRepository usersRepository;
    @Autowired
    public JWTTokenProvider(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(10).toInstant());

        String userId = Long.toString(user.getId());
        return JWT.create()
                .withSubject("User details")
                .withClaim("id", userId)
                .withClaim("username", user.getEmail())
                .withIssuedAt(new Date())
                .withIssuer("instazoo")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }
    public Long getUserIdFromToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("instazoo")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        Long userId = Long.parseLong(jwt.getClaim("id").asString());
        return userId;
    }
}
