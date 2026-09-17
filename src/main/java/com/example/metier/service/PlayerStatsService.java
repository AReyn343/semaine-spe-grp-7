package com.example.metier.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.metier.dto.GlobalStatsDto;
import com.example.metier.dto.PlayerStatsDto;
import com.example.util.MapperUtil;

@Service
public class PlayerStatsService {

    private final DataLoaderService dataLoader;

    public PlayerStatsService(DataLoaderService dataLoader) {
        this.dataLoader = dataLoader;
    }

    public Optional<PlayerStatsDto> findByPlayerId(String playerId) {
        return dataLoader.getPlayerStats().stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .map(MapperUtil::toPlayerStatsDto)
                .findFirst();
    }

    public List<PlayerStatsDto> findAll() {
        return dataLoader.getPlayerStats().stream()
                .map(MapperUtil::toPlayerStatsDto)
                .collect(Collectors.toList());
    }

    public List<PlayerStatsDto> topByElo(int limit) {
        return dataLoader.getPlayerStats().stream()
                .sorted(Comparator.comparingInt(p -> -p.getElo()))
                .limit(limit)
                .map(MapperUtil::toPlayerStatsDto)
                .collect(Collectors.toList());
    }

    public long countActive() {
        return dataLoader.getPlayerStats().stream()
                .filter(p -> p.getTotalMatches() > 0)
                .count();
    }

    public double averageElo() {
        return dataLoader.getPlayerStats().stream()
                .mapToInt(p -> p.getElo())
                .average()
                .orElse(0.0);
    }

    public GlobalStatsDto getGlobalStats() {
        var players = dataLoader.getPlayerStats();
        GlobalStatsDto dto = new GlobalStatsDto();

        dto.setTotalPlayers(players.size());

        dto.setAvgElo(players.stream().mapToInt(p -> p.getElo()).average().orElse(0));

        dto.setAvgKda(players.stream()
                .mapToDouble(p -> (p.getKills() + p.getAssists()) / (double) Math.max(1, p.getDeaths()))
                .average().orElse(0));

        dto.setAvgCsPerMin(players.stream()
                .filter(p -> p.getPlayTimeSeconds() > 60)
                .mapToDouble(p -> p.getCs() / (p.getPlayTimeSeconds() / 60.0))
                .average().orElse(0));

        dto.setAvgWinRate(players.stream().mapToDouble(p -> p.getWinRate()).average().orElse(0));

        dto.setTopTier(players.stream()
                .collect(Collectors.groupingBy(p -> p.getTier(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("N/A"));

        dto.setMostPlayedChampion(players.stream()
                .collect(Collectors.groupingBy(p -> p.getChampion(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("N/A"));

        dto.setTotalCoinsInCirculation(players.stream()
                .filter(p -> p.getWallet() != null)
                .mapToInt(p -> p.getWallet().getOrDefault("coins", 0))
                .sum());

        dto.setPlayersByTier(players.stream()
                .collect(Collectors.groupingBy(p -> p.getTier(), Collectors.counting())));

        dto.setPlayersByLane(players.stream()
                .collect(Collectors.groupingBy(p -> p.getLane(), Collectors.counting())));

        dto.setTop3ByElo(topByElo(3));

        return dto;
    }
}