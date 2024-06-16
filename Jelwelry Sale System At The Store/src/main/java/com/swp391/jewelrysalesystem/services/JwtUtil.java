package com.swp391.jewelrysalesystem.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your_secret_key_your_secret_key_your_secret_key_your_secret_key";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String extractUsername(String token) {
        return (String) extractAllClaims(token).get("sub");
    }

    public Date extractExpiration(String token) {
        // Cast the expiration claim to a Number and convert to a Long
        Number expiration = (Number) extractAllClaims(token).get("exp");
        return new Date(expiration.longValue() * 1000); // Convert seconds to milliseconds
    }

    public <T> T extractClaim(String token, String claim) {
        return (T) extractAllClaims(token).get(claim);
    }

    private Map<String, Object> extractAllClaims(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(parts[1]));
            return objectMapper.readValue(payload, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT token", e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    @SuppressWarnings("deprecation")
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
