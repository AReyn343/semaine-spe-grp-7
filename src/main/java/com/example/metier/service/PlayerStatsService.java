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
