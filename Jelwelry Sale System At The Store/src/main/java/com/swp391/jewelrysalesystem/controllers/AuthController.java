package com.swp391.jewelrysalesystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import com.swp391.jewelrysalesystem.models.AuthenticationRequest;
import com.swp391.jewelrysalesystem.models.AuthenticationResponse;
import com.swp391.jewelrysalesystem.models.ForgotPasswordRequest;
import com.swp391.jewelrysalesystem.models.ResetPasswordRequest;
import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.EmailService;
import com.swp391.jewelrysalesystem.services.JwtUtil;
import com.swp391.jewelrysalesystem.services.UserService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                jwtUtil.invalidateToken(jwt);
                LOGGER.info("JWT Token invalidated: " + jwt);
                return ResponseEntity.ok("Logged out successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
            }
        } catch (Exception e) {
            LOGGER.severe("Exception during logout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            User user = userService.getUserByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            String token = jwtUtil.generatePasswordResetToken(user.getEmail());
            String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;

            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
            LOGGER.info("Password reset email sent to: " + user.getEmail());
            return ResponseEntity.ok("Password reset email sent.");
        } catch (Exception e) {
            LOGGER.severe("Exception during forgot password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process forgot password.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request,
            @RequestParam("token") String token) {
        try {
            String email = jwtUtil.validatePasswordResetToken(token);
            if (email == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
            }

            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            user.setPassword(request.getNewPassword());
            userService.saveUser(user);

            LOGGER.info("Password reset successfully for: " + user.getEmail());
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            LOGGER.severe("Exception during password reset: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password.");
        }
    }

}
