package com.swp391.jewelrysalesystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import com.swp391.jewelrysalesystem.models.AuthenticationRequest;
import com.swp391.jewelrysalesystem.models.AuthenticationResponse;
import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.JwtUtil;
import com.swp391.jewelrysalesystem.services.UserService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            // Authenticate the user using email and password
            User user = userService.getUserByEmailAndPassword(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword());
            if (user == null) {
                LOGGER.warning("Authentication failed for user: " + authenticationRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(null, 500));
            }

            // Generate JWT token
            final String jwt = jwtUtil.generateToken(user.getEmail());
            LOGGER.info("JWT Token generated for user: " + authenticationRequest.getEmail());
            return ResponseEntity.ok(new AuthenticationResponse(jwt, 200));
        } catch (Exception e) {
            LOGGER.severe("Exception during authentication: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(null, 500));
        }
    }

}
