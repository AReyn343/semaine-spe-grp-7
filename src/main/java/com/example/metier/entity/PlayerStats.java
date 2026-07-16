package com.example.metier.entity;

import java.util.List;
import java.util.Map;

public class PlayerStats {

    private String playerId;
    private String username;
    private double winRate;
    private double averageScore;   // KDA
    private int    elo;
    private int    playTimeSeconds;
    private int    totalMatches;
    private String favoriteMode;

    // Champs LoL
    private String champion;
    private String lane;
    private String tier;
    private int    rank;
    private int    kills;
    private int    deaths;
    private int    assists;
    private int    cs;
    private int    visionScore;
    private int    damageDealt;

    // Domaine Économie (§D5)
    private Map<String, Integer> wallet;   // { coins, premiumCoins }
    private List<String>         items;    // items possédés

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
    public String getChampion() { return champion; }
    public void setChampion(String champion) { this.champion = champion; }
    public String getLane() { return lane; }
    public void setLane(String lane) { this.lane = lane; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    public int getKills() { return kills; }
    public void setKills(int kills) { this.kills = kills; }
    public int getDeaths() { return deaths; }
    public void setDeaths(int deaths) { this.deaths = deaths; }
    public int getAssists() { return assists; }
    public void setAssists(int assists) { this.assists = assists; }
    public int getCs() { return cs; }
    public void setCs(int cs) { this.cs = cs; }
    public int getVisionScore() { return visionScore; }
    public void setVisionScore(int visionScore) { this.visionScore = visionScore; }
    public int getDamageDealt() { return damageDealt; }
    public void setDamageDealt(int damageDealt) { this.damageDealt = damageDealt; }
    public Map<String, Integer> getWallet() { return wallet; }
    public void setWallet(Map<String, Integer> wallet) { this.wallet = wallet; }
    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}
