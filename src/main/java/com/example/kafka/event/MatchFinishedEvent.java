package com.example.kafka.event;
import java.time.LocalDateTime;
public class MatchFinishedEvent {
    private String matchId, mode, region;
    private int scoreTeamA, scoreTeamB, durationSeconds;
    private LocalDateTime occurredAt;
    public MatchFinishedEvent() {}
    public MatchFinishedEvent(String matchId, String mode, String region, int scoreTeamA, int scoreTeamB, int durationSeconds) {
        this.matchId = matchId; this.mode = mode; this.region = region;
        this.scoreTeamA = scoreTeamA; this.scoreTeamB = scoreTeamB;
        this.durationSeconds = durationSeconds; this.occurredAt = LocalDateTime.now();
    }
    public String getMatchId() { return matchId; } public void setMatchId(String v) { matchId = v; }
    public String getMode() { return mode; } public void setMode(String v) { mode = v; }
    public String getRegion() { return region; } public void setRegion(String v) { region = v; }
    public int getScoreTeamA() { return scoreTeamA; } public void setScoreTeamA(int v) { scoreTeamA = v; }
    public int getScoreTeamB() { return scoreTeamB; } public void setScoreTeamB(int v) { scoreTeamB = v; }
    public int getDurationSeconds() { return durationSeconds; } public void setDurationSeconds(int v) { durationSeconds = v; }
    public LocalDateTime getOccurredAt() { return occurredAt; } public void setOccurredAt(LocalDateTime v) { occurredAt = v; }
}
