package com.example.instazoo_app.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.services.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JWTAuthenticationFilter(JWTTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED
                        , "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                    User userDetails = customUserDetailsService.loadUserById(userId);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails
                                    , userDetails.getPassword()
                                    , userDetails.getAuthorities());
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException exs) {
                    log.error("Could not set user authentication");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
    //рефакторинг с отдельным методом для доставания JWT из хэдера
     /*try {
        String jwt = getJWTFromRequest(request);
        if (!jwt.isBlank()) {
            Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
            User userDetails = customUserDetailsService.loadUserById(userId);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails
                            , userDetails.getPassword()
                            , userDetails.getAuthorities());
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    } catch (
    Exception exs) {
        LOG.error("Could not set user authentication");
    }
        filterChain.doFilter(request, response);
}
    private String getJWTFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }*/
}
