package com.swp391.jewelrysalesystem.filters;

import com.swp391.jewelrysalesystem.services.JwtUtil;
import com.swp391.jewelrysalesystem.services.UserService;
import com.swp391.jewelrysalesystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(JwtRequestFilter.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        LOGGER.info("Authorization Header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                LOGGER.severe("JWT Token extraction failed: " + e.getMessage());
            }
            LOGGER.info("JWT Token extracted: " + jwt);
            LOGGER.info("Email extracted from JWT: " + email);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = null;
            try {
                user = userService.getUserByEmail(email);
                LOGGER.info("User retrieved: " + (user != null ? user.getEmail() : "null"));
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.severe("Error retrieving user by email: " + e.getMessage());
            }

            if (user != null && jwtUtil.validateToken(jwt, user.getEmail())) {
                LOGGER.info("JWT Token validated for user: " + email);

                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                switch (user.getRoleID()) {
                    case 1:
                        authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
                        break;
                    case 2:
                        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
                        break;
                    case 3:
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid role ID: " + user.getRoleID());
                }

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, authorities);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                LOGGER.warning("JWT Token validation failed for user: " + email);
            }
        }
        chain.doFilter(request, response);
    }
}
