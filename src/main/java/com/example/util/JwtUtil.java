package com.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utilitaire JWT — génère et valide des tokens HS256.
 * En production : utiliser RS256 avec paire de clés asymétrique.
 */
@Component
public class JwtUtil {

    // Clé secrète dérivée pour HS256 (256 bits minimum)
    private final Key secretKey = Keys.hmacShaKeyFor(
        "groupe7-monitoring-jwt-secret-key-sup-de-vinci-2026".getBytes()
    );

    private static final long EXPIRATION_MS = 3600_000L; // 1 heure

    /** Génère un JWT signé avec username et role dans les claims */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("app", "groupe7-monitoring")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extrait le username (subject) du token */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /** Extrait le rôle du token */
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    /** Vérifie si le token est expiré */
    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
