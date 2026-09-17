package com.example.kafka.event;
import java.time.LocalDateTime;
public class ServerOverloadedEvent {
    private String serverId, region;
    private double load;
    private int connectedPlayers;
    private LocalDateTime occurredAt;
    public ServerOverloadedEvent() {}
    public ServerOverloadedEvent(String serverId, String region, double load, int connectedPlayers) {
        this.serverId = serverId; this.region = region; this.load = load;
        this.connectedPlayers = connectedPlayers; this.occurredAt = LocalDateTime.now();
    }
    public String getServerId() { return serverId; } public void setServerId(String v) { serverId = v; }
    public String getRegion() { return region; } public void setRegion(String v) { region = v; }
    public double getLoad() { return load; } public void setLoad(double v) { load = v; }
    public int getConnectedPlayers() { return connectedPlayers; } public void setConnectedPlayers(int v) { connectedPlayers = v; }
    public LocalDateTime getOccurredAt() { return occurredAt; } public void setOccurredAt(LocalDateTime v) { occurredAt = v; }
}
