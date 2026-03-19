package com.example.metier.service;

import com.example.metier.dto.MatchDto;
import com.example.metier.dto.MatchFilterDto;
import com.example.metier.entity.Match;
import com.example.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final DataLoaderService dataLoader;

    public MatchService(DataLoaderService dataLoader) {
        this.dataLoader = dataLoader;
    }

    /** Tous les matchs */
    public List<MatchDto> findAll() {
        return dataLoader.getMatches().stream()
                .map(MapperUtil::toMatchDto)
                .collect(Collectors.toList());
    }

    /** Un match par ID */
    public Optional<MatchDto> findById(String id) {
        return dataLoader.getMatches().stream()
                .filter(m -> m.getId().equals(id))
                .map(MapperUtil::toMatchDto)
                .findFirst();
    }

    /** Recherche filtrée */
    public List<MatchDto> search(MatchFilterDto filter) {
        return dataLoader.getMatches().stream()
                .filter(m -> filter.getMode()   == null || filter.getMode().equalsIgnoreCase(m.getMode()))
                .filter(m -> filter.getStatus() == null || filter.getStatus().equalsIgnoreCase(m.getStatus()))
                .filter(m -> filter.getRegion() == null || filter.getRegion().equalsIgnoreCase(m.getRegion()))
                .filter(m -> filter.getMinDuration() == null || m.getDurationSeconds() >= filter.getMinDuration())
                .filter(m -> filter.getMaxDuration() == null || m.getDurationSeconds() <= filter.getMaxDuration())
                .map(MapperUtil::toMatchDto)
                .collect(Collectors.toList());
    }

    /** Nombre total de matchs */
    public int countTotal() {
        return dataLoader.getMatches().size();
    }

    /** Nombre de matchs en cours */
    public long countActive() {
        return dataLoader.getMatches().stream()
                .filter(m -> "IN_PROGRESS".equals(m.getStatus()))
                .count();
    }

    /** Durée moyenne des matchs terminés (en secondes) */
    public double averageDurationSeconds() {
        return dataLoader.getMatches().stream()
                .filter(m -> "FINISHED".equals(m.getStatus()))
                .mapToInt(Match::getDurationSeconds)
                .average()
                .orElse(0.0);
    }
}
