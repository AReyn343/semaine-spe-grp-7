package com.example.kafka.event;
import java.time.LocalDateTime;
public class ScoreUpdatedEvent {
    private String playerId, username, matchId;
    private int oldElo, newElo, eloDelta;
    private LocalDateTime occurredAt;
    public ScoreUpdatedEvent() {}
    public ScoreUpdatedEvent(String playerId, String username, int oldElo, int newElo, String matchId) {
        this.playerId = playerId; this.username = username;
        this.oldElo = oldElo; this.newElo = newElo; this.eloDelta = newElo - oldElo;
        this.matchId = matchId; this.occurredAt = LocalDateTime.now();
    }
    public String getPlayerId() { return playerId; } public void setPlayerId(String v) { playerId = v; }
    public String getUsername() { return username; } public void setUsername(String v) { username = v; }
    public String getMatchId() { return matchId; } public void setMatchId(String v) { matchId = v; }
    public int getOldElo() { return oldElo; } public void setOldElo(int v) { oldElo = v; }
    public int getNewElo() { return newElo; } public void setNewElo(int v) { newElo = v; }
    public int getEloDelta() { return eloDelta; } public void setEloDelta(int v) { eloDelta = v; }
    public LocalDateTime getOccurredAt() { return occurredAt; } public void setOccurredAt(LocalDateTime v) { occurredAt = v; }
}
