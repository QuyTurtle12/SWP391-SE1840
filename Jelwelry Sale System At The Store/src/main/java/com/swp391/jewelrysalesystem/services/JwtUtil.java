package com.swp391.jewelrysalesystem.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your_secret_key_your_secret_key_your_secret_key_your_secret_key";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Set<String> invalidatedTokens = new HashSet<>();

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

    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
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
        return (extractedUsername.equals(username) && !isTokenExpired(token) && !isTokenInvalidated(token));
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    private boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }

    public String generatePasswordResetToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        return createPasswordResetToken(claims, email);
    }

    private String createPasswordResetToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutes expiry
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String validatePasswordResetToken(String token) {
        try {
            String email = extractUsername(token);
            if (isTokenExpired(token)) {
                return null;
            }
            return email;
        } catch (Exception e) {
            return null;
        }
    }

}
