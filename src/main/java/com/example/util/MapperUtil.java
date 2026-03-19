package com.example.util;

import com.example.metier.dto.MatchDto;
import com.example.metier.dto.PlayerStatsDto;
import com.example.metier.entity.Match;
import com.example.metier.entity.PlayerStats;

public class MapperUtil {

    private MapperUtil() {}

    // ── Match ────────────────────────────────────────────────────
    public static MatchDto toMatchDto(Match match) {
        MatchDto dto = new MatchDto();
        dto.setId(match.getId());
        dto.setMode(match.getMode());
        dto.setStatus(match.getStatus());
        dto.setDurationSeconds(match.getDurationSeconds());
        dto.setRegion(match.getRegion());
        dto.setServerId(match.getServerId());
        dto.setScoreTeamA(match.getScoreTeamA());
        dto.setScoreTeamB(match.getScoreTeamB());
        dto.setCreatedAt(match.getCreatedAt());
        return dto;
    }

    // ── PlayerStats ──────────────────────────────────────────────
    public static PlayerStatsDto toPlayerStatsDto(PlayerStats p) {
        PlayerStatsDto dto = new PlayerStatsDto();
        dto.setPlayerId(p.getPlayerId());
        dto.setUsername(p.getUsername());
        dto.setWinRate(p.getWinRate());
        dto.setAverageScore(p.getAverageScore());
        dto.setElo(p.getElo());
        dto.setPlayTimeSeconds(p.getPlayTimeSeconds());
        dto.setTotalMatches(p.getTotalMatches());
        dto.setFavoriteMode(p.getFavoriteMode());
        return dto;
    }
}
