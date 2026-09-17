package com.example.kafka.event;
import java.time.LocalDateTime;
public class PlayerBannedEvent {
    private String playerId, username, reason;
    private int durationHours;
    private LocalDateTime occurredAt;
    public PlayerBannedEvent() {}
    public PlayerBannedEvent(String playerId, String username, String reason, int durationHours) {
        this.playerId = playerId; this.username = username; this.reason = reason;
        this.durationHours = durationHours; this.occurredAt = LocalDateTime.now();
    }
    public String getPlayerId() { return playerId; } public void setPlayerId(String v) { playerId = v; }
    public String getUsername() { return username; } public void setUsername(String v) { username = v; }
    public String getReason() { return reason; } public void setReason(String v) { reason = v; }
    public int getDurationHours() { return durationHours; } public void setDurationHours(int v) { durationHours = v; }
    public LocalDateTime getOccurredAt() { return occurredAt; } public void setOccurredAt(LocalDateTime v) { occurredAt = v; }
}
