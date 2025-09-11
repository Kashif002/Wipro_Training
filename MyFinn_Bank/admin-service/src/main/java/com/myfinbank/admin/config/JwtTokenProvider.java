package com.myfinbank.admin.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private Key getSigningKey() {
        // Create signing key from secret
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    // Generate JWT token for authenticated admin
    public String generateToken(Authentication authentication) {
        String email = authentication.getName(); // Admin email
        Date expirationDate = new Date(System.currentTimeMillis() + jwtConfig.getExpiration());

        return Jwts.builder()
                .setSubject(email) // Admin email as subject
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract email from JWT token
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid
            return false;
        }
    }
}
