package com.example.metier.dto;

import java.util.List;
import java.util.Map;

public class GlobalStatsDto {

    private int    totalPlayers;
    private double avgElo;
    private double avgKda;
    private double avgCsPerMin;
    private double avgWinRate;
    private String topTier;          // tier le plus représenté
    private String mostPlayedChampion;
    private int    totalCoinsInCirculation;
    private Map<String, Long>   playersByTier;
    private Map<String, Long>   playersByLane;
    private List<PlayerStatsDto> top3ByElo;

    public GlobalStatsDto() {}

    public int getTotalPlayers() { return totalPlayers; }
    public void setTotalPlayers(int totalPlayers) { this.totalPlayers = totalPlayers; }
    public double getAvgElo() { return avgElo; }
    public void setAvgElo(double avgElo) { this.avgElo = avgElo; }
    public double getAvgKda() { return avgKda; }
    public void setAvgKda(double avgKda) { this.avgKda = avgKda; }
    public double getAvgCsPerMin() { return avgCsPerMin; }
    public void setAvgCsPerMin(double avgCsPerMin) { this.avgCsPerMin = avgCsPerMin; }
    public double getAvgWinRate() { return avgWinRate; }
    public void setAvgWinRate(double avgWinRate) { this.avgWinRate = avgWinRate; }
    public String getTopTier() { return topTier; }
    public void setTopTier(String topTier) { this.topTier = topTier; }
    public String getMostPlayedChampion() { return mostPlayedChampion; }
    public void setMostPlayedChampion(String mostPlayedChampion) { this.mostPlayedChampion = mostPlayedChampion; }
    public int getTotalCoinsInCirculation() { return totalCoinsInCirculation; }
    public void setTotalCoinsInCirculation(int totalCoinsInCirculation) { this.totalCoinsInCirculation = totalCoinsInCirculation; }
    public Map<String, Long> getPlayersByTier() { return playersByTier; }
    public void setPlayersByTier(Map<String, Long> playersByTier) { this.playersByTier = playersByTier; }
    public Map<String, Long> getPlayersByLane() { return playersByLane; }
    public void setPlayersByLane(Map<String, Long> playersByLane) { this.playersByLane = playersByLane; }
    public List<PlayerStatsDto> getTop3ByElo() { return top3ByElo; }
    public void setTop3ByElo(List<PlayerStatsDto> top3ByElo) { this.top3ByElo = top3ByElo; }
}
