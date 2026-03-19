package com.example.metier.entity;

public class PlayerStats {

    private String playerId;
    private String username;
    private double winRate;       // 0.0 → 1.0
    private double averageScore;
    private int    elo;
    private int    playTimeSeconds;
    private int    totalMatches;
    private String favoriteMode;

    public PlayerStats() {}

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public double getWinRate() { return winRate; }
    public void setWinRate(double winRate) { this.winRate = winRate; }

    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }

    public int getElo() { return elo; }
    public void setElo(int elo) { this.elo = elo; }

    public int getPlayTimeSeconds() { return playTimeSeconds; }
    public void setPlayTimeSeconds(int playTimeSeconds) { this.playTimeSeconds = playTimeSeconds; }

    public int getTotalMatches() { return totalMatches; }
    public void setTotalMatches(int totalMatches) { this.totalMatches = totalMatches; }

    public String getFavoriteMode() { return favoriteMode; }
    public void setFavoriteMode(String favoriteMode) { this.favoriteMode = favoriteMode; }
}
