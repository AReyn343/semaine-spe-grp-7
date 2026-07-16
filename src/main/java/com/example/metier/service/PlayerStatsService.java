package com.example.metier.service;

import com.example.metier.dto.PlayerStatsDto;
import com.example.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerStatsService {

    private final DataLoaderService dataLoader;

    public PlayerStatsService(DataLoaderService dataLoader) {
        this.dataLoader = dataLoader;
    }

    /** Stats d'un joueur par ID */
    public Optional<PlayerStatsDto> findByPlayerId(String playerId) {
        return dataLoader.getPlayerStats().stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .map(MapperUtil::toPlayerStatsDto)
                .findFirst();
    }

    /** Tous les joueurs */
    public List<PlayerStatsDto> findAll() {
        return dataLoader.getPlayerStats().stream()
                .map(MapperUtil::toPlayerStatsDto)
                .collect(Collectors.toList());
    }

    /** Top N joueurs par ELO */
    public List<PlayerStatsDto> topByElo(int limit) {
        return dataLoader.getPlayerStats().stream()
                .sorted(Comparator.comparingInt(p -> -p.getElo()))
                .limit(limit)
                .map(MapperUtil::toPlayerStatsDto)
                .collect(Collectors.toList());
    }

    /** Nombre de joueurs actifs (ayant au moins 1 match) */
    public long countActive() {
        return dataLoader.getPlayerStats().stream()
                .filter(p -> p.getTotalMatches() > 0)
                .count();
    }

    /** ELO moyen de tous les joueurs */
    public double averageElo() {
        return dataLoader.getPlayerStats().stream()
                .mapToInt(p -> p.getElo())
                .average()
                .orElse(0.0);
    }
}

    // Méthode ajoutée par Momo — stats globales agrégées
    public com.example.metier.dto.GlobalStatsDto getGlobalStats() {
        java.util.List<com.example.metier.entity.PlayerStats> players = dataLoader.getPlayerStats();
        com.example.metier.dto.GlobalStatsDto dto = new com.example.metier.dto.GlobalStatsDto();

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

        // Tier le plus représenté
        dto.setTopTier(players.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                p -> p.getTier(), java.util.stream.Collectors.counting()))
            .entrySet().stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(java.util.Map.Entry::getKey).orElse("N/A"));

        // Champion le plus joué
        dto.setMostPlayedChampion(players.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                p -> p.getChampion(), java.util.stream.Collectors.counting()))
            .entrySet().stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(java.util.Map.Entry::getKey).orElse("N/A"));

        // Total coins en circulation
        dto.setTotalCoinsInCirculation(players.stream()
            .filter(p -> p.getWallet() != null)
            .mapToInt(p -> p.getWallet().getOrDefault("coins", 0))
            .sum());

        // Répartition par tier
        dto.setPlayersByTier(players.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                p -> p.getTier(), java.util.stream.Collectors.counting())));

        // Répartition par lane
        dto.setPlayersByLane(players.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                p -> p.getLane(), java.util.stream.Collectors.counting())));

        // Top 3 par ELO
        dto.setTop3ByElo(topByElo(3));

        return dto;
    }
