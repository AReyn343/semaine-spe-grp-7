package com.example.metier.dto;

public class AuthResponseDto {
    private String token;
    private String username;
    private String role;
    private int    expiresIn;
    private String tokenType = "Bearer";

    public AuthResponseDto(String token, String username, String role, int expiresIn) {
        this.token     = token;
        this.username  = username;
        this.role      = role;
        this.expiresIn = expiresIn;
    }

    public String getToken()     { return token; }
    public String getUsername()  { return username; }
    public String getRole()      { return role; }
    public int    getExpiresIn() { return expiresIn; }
    public String getTokenType() { return tokenType; }
}
