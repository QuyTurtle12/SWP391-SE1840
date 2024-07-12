package com.swp391.jewelrysalesystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.swp391.jewelrysalesystem.models.AuthenticationRequest;
import com.swp391.jewelrysalesystem.models.AuthenticationResponse;
import com.swp391.jewelrysalesystem.models.ForgotPasswordRequest;
import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.EmailService;
import com.swp391.jewelrysalesystem.services.JwtUtil;
import com.swp391.jewelrysalesystem.services.PasswordService;
import com.swp391.jewelrysalesystem.services.UserService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            User user = userService.getUserByEmailAndPassword(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword());
            if (user == null) {
                LOGGER.warning("Authentication failed for user: " + authenticationRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(null, 500));
            }

            List<String> roles = List.of(user.getRoleID() == 1 ? "ROLE_STAFF"
                    : user.getRoleID() == 2 ? "ROLE_MANAGER" : user.getRoleID() == 3 ? "ROLE_ADMIN" : "");

            final String jwt = jwtUtil.generateToken(user.getEmail(),
                    String.valueOf(userService.getUserByEmail(user.getEmail()).getID()), roles);
            LOGGER.info("JWT Token generated for user: " + authenticationRequest.getEmail() + " with roles: " + roles);

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
        LOGGER.info("Received forgot password request for email: " + request.getEmail());
        try {
            if (!userService.checkIfEmailExists(request.getEmail())) {
                LOGGER.warning("User not found for email: " + request.getEmail());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            String token = jwtUtil.generatePasswordResetToken(request.getEmail());
            String resetLink = "https://jewelry-sale-system.vercel.app/reset-password?token=" + token;

            emailService.sendPasswordResetEmail(request.getEmail(), resetLink);
            LOGGER.info("Password reset email sent to: " + request.getEmail());
            return ResponseEntity.ok("Password reset email sent.");
        } catch (Exception e) {
            LOGGER.severe("Exception during forgot password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process forgot password.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) {
        try {
            String email = jwtUtil.validatePasswordResetToken(token);
            if (email == null) {
                LOGGER.warning("Invalid or expired token.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
            }

            User user = userService.getUserByEmail(email);
            if (user == null) {
                LOGGER.warning("User not found for email: " + email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            user.setPassword(passwordService.hashPassword(newPassword));
            userService.saveUser(user);

            LOGGER.info("Password reset successfully for: " + email);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            LOGGER.severe("Exception during password reset: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password.");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/data")
    public ResponseEntity<?> getAdminData() {
        return ResponseEntity.ok("Admin data");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/manager/data")
    public ResponseEntity<?> getManagerData() {
        return ResponseEntity.ok("Manager data");
    }

}
