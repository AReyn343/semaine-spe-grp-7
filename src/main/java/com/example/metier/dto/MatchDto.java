package com.example.metier.dto;

import java.time.LocalDateTime;

public class MatchDto {
    private String id;
    private String mode;
    private String status;
    private int durationSeconds;
    private String region;
    private String serverId;
    private int scoreTeamA;
    private int scoreTeamB;
    private LocalDateTime createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }
    public int getScoreTeamA() { return scoreTeamA; }
    public void setScoreTeamA(int scoreTeamA) { this.scoreTeamA = scoreTeamA; }
    public int getScoreTeamB() { return scoreTeamB; }
    public void setScoreTeamB(int scoreTeamB) { this.scoreTeamB = scoreTeamB; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
