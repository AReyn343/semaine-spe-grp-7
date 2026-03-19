package com.example.metier.dto;

import java.util.List;

public class DashboardDto {
    private int totalMatches;
    private int activeMatches;
    private int activePlayers;
    private double averageMatchDurationSeconds;
    private List<ServerLoadDto> serverLoads;
    private List<PlayerStatsDto> topPlayers;
    private String platformHealth;   // HEALTHY, DEGRADED, DOWN
    private List<KpiDto> kpis;

    public int getTotalMatches() { return totalMatches; }
    public void setTotalMatches(int totalMatches) { this.totalMatches = totalMatches; }
    public int getActiveMatches() { return activeMatches; }
    public void setActiveMatches(int activeMatches) { this.activeMatches = activeMatches; }
    public int getActivePlayers() { return activePlayers; }
    public void setActivePlayers(int activePlayers) { this.activePlayers = activePlayers; }
    public double getAverageMatchDurationSeconds() { return averageMatchDurationSeconds; }
    public void setAverageMatchDurationSeconds(double v) { this.averageMatchDurationSeconds = v; }
    public List<ServerLoadDto> getServerLoads() { return serverLoads; }
    public void setServerLoads(List<ServerLoadDto> serverLoads) { this.serverLoads = serverLoads; }
    public List<PlayerStatsDto> getTopPlayers() { return topPlayers; }
    public void setTopPlayers(List<PlayerStatsDto> topPlayers) { this.topPlayers = topPlayers; }
    public String getPlatformHealth() { return platformHealth; }
    public void setPlatformHealth(String platformHealth) { this.platformHealth = platformHealth; }
    public List<KpiDto> getKpis() { return kpis; }
    public void setKpis(List<KpiDto> kpis) { this.kpis = kpis; }
}