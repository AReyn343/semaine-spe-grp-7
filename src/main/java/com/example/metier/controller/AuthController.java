package com.example.metier.controller;

import com.example.metier.dto.AuthRequestDto;
import com.example.metier.dto.AuthResponseDto;
import com.example.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /api/v1/auth/login
     * Authentification simplifiée — retourne un JWT signé HS256.
     * En production : vérification BDD + BCrypt + refresh token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
        // Utilisateurs hardcodés pour la démo
        Map<String, String> users = Map.of(
            "admin", "admin123",
            "ali",   "password123"
        );

        String expectedPassword = users.get(request.getUsername());
        if (expectedPassword == null || !expectedPassword.equals(request.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "error", Map.of("code", "INVALID_CREDENTIALS", "message", "Identifiants invalides")
            ));
        }

        String role  = "admin".equals(request.getUsername()) ? "ADMIN" : "PLAYER";
        String token = jwtUtil.generateToken(request.getUsername(), role);

        return ResponseEntity.ok(new AuthResponseDto(token, request.getUsername(), role, 3600));
    }

    /**
     * GET /api/v1/auth/me
     * Retourne les informations du token (demo — pas de vraie validation).
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Token manquant"));
        }
        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            String role     = jwtUtil.extractRole(token);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of("username", username, "role", role)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token invalide"));
        }
    }
}
