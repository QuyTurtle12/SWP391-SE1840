package com.swp391.jewelrysalesystem.services;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    // Method to hash a password
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Method to verify a password against a hash
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
