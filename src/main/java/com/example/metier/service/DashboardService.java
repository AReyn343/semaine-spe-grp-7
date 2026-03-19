package com.example.metier.service;

import com.example.metier.dto.DashboardDto;
import com.example.metier.dto.KpiDto;
import com.example.metier.dto.ServerLoadDto;
import com.example.metier.entity.ServerStatus;
import com.example.monitoring.service.AlertService;
import com.example.monitoring.service.MonitoringService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Point d'entrée central du dashboard.
 * Orchestre MatchService, PlayerStatsService, DataLoaderService
 * et appelle MonitoringService pour enrichir la vue avec les métriques.
 */
@Service
public class DashboardService {

    private final MatchService       matchService;
    private final PlayerStatsService playerStatsService;
    private final DataLoaderService  dataLoader;
    private final MonitoringService  monitoringService;
    private final AlertService       alertService;

    public DashboardService(MatchService matchService,
                            PlayerStatsService playerStatsService,
                            DataLoaderService dataLoader,
                            @Lazy MonitoringService monitoringService,
                            @Lazy AlertService alertService) {
        this.matchService       = matchService;
        this.playerStatsService = playerStatsService;
        this.dataLoader         = dataLoader;
        this.monitoringService  = monitoringService;
        this.alertService       = alertService;
    }

    public DashboardDto buildDashboard() {

        // Comptabilise l'appel dans Micrometer
        monitoringService.recordDashboardRequest();

        DashboardDto dto = new DashboardDto();

        // ── Métriques matchs ──────────────────────────────────────
        dto.setTotalMatches(matchService.countTotal());
        dto.setActiveMatches((int) matchService.countActive());
        dto.setAverageMatchDurationSeconds(matchService.averageDurationSeconds());

        // ── Métriques joueurs ─────────────────────────────────────
        dto.setActivePlayers((int) playerStatsService.countActive());
        dto.setTopPlayers(playerStatsService.topByElo(5));

        // ── Charge serveurs ───────────────────────────────────────
        List<ServerLoadDto> loads = dataLoader.getServers().stream()
                .map(this::toServerLoadDto)
                .collect(Collectors.toList());
        dto.setServerLoads(loads);

        // ── Santé globale ─────────────────────────────────────────
        dto.setPlatformHealth(computePlatformHealth(dataLoader.getServers()));

        // ── KPIs enrichis avec données monitoring ─────────────────
        long openAlerts      = alertService.countOpenAlerts();
        int  connectedPlayers = loads.stream().mapToInt(ServerLoadDto::getConnectedPlayers).sum();
        double avgLoad       = loads.stream().mapToDouble(ServerLoadDto::getLoad).average().orElse(0);

        dto.setKpis(List.of(
            new KpiDto("Joueurs connectés",   connectedPlayers,                              "joueurs"),
            new KpiDto("ELO moyen",           Math.round(playerStatsService.averageElo()),   "pts"),
            new KpiDto("Charge serveurs",     String.format("%.0f%%", avgLoad * 100),        ""),
            new KpiDto("Alertes ouvertes",    openAlerts,                                    "alertes"),
            new KpiDto("Serveurs actifs",     loads.size(),                                  "serveurs"),
            new KpiDto("Serveurs dégradés",   countDegraded(dataLoader.getServers()),        "serveurs")
        ));

        return dto;
    }

    // ── Helpers ───────────────────────────────────────────────────

    private ServerLoadDto toServerLoadDto(ServerStatus s) {
        ServerLoadDto dto = new ServerLoadDto();
        dto.setServerId(s.getServerId());
        dto.setRegion(s.getRegion());
        dto.setLoad(s.getLoad());
        dto.setHealthStatus(s.getHealthStatus());
        dto.setActiveMatches(s.getActiveMatches());
        dto.setConnectedPlayers(s.getConnectedPlayers());
        return dto;
    }

    private String computePlatformHealth(List<ServerStatus> servers) {
        long down     = servers.stream().filter(s -> "DOWN".equals(s.getHealthStatus())).count();
        long degraded = servers.stream().filter(s -> "DEGRADED".equals(s.getHealthStatus())).count();
        if (down > 0)     return "DOWN";
        if (degraded > 0) return "DEGRADED";
        return "HEALTHY";
    }

    private long countDegraded(List<ServerStatus> servers) {
        return servers.stream()
                .filter(s -> "DEGRADED".equals(s.getHealthStatus()) || "DOWN".equals(s.getHealthStatus()))
                .count();
    }
}
