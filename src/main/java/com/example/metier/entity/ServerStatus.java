package com.example.metier.entity;

public class ServerStatus {

    private String serverId;
    private String region;
    private double load;           // 0.0 → 1.0
    private String healthStatus;   // HEALTHY, DEGRADED, DOWN
    private int    activeMatches;
    private int    connectedPlayers;

    public ServerStatus() {}

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
