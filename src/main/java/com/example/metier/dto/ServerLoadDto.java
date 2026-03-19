package com.example.metier.dto;

public class ServerLoadDto {
    private String serverId;
    private String region;
    private double load;
    private String healthStatus;
    private int activeMatches;
    private int connectedPlayers;

    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public double getLoad() { return load; }
    public void setLoad(double load) { this.load = load; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public int getActiveMatches() { return activeMatches; }
    public void setActiveMatches(int activeMatches) { this.activeMatches = activeMatches; }
    public int getConnectedPlayers() { return connectedPlayers; }
    public void setConnectedPlayers(int connectedPlayers) { this.connectedPlayers = connectedPlayers; }
}