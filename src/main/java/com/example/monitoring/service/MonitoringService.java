package com.example.monitoring.service;

import com.example.metier.service.DashboardService;
import com.example.metier.service.DataLoaderService;
import com.example.metier.service.MatchService;
import com.example.metier.service.PlayerStatsService;
import com.example.monitoring.dto.MetricSampleDto;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Cœur du monitoring.
 * Enregistre les métriques Micrometer custom et fournit un snapshot
 * des valeurs courantes pour le dashboard et les alertes.
 */
@Service
public class MonitoringService {

    private final MeterRegistry      registry;
    private final MatchService        matchService;
    private final PlayerStatsService  playerStatsService;
    private final DataLoaderService   dataLoader;

    // Compteurs
    private Counter dashboardRequestCounter;

    public MonitoringService(MeterRegistry registry,
                             MatchService matchService,
                             PlayerStatsService playerStatsService,
                             DataLoaderService dataLoader) {
        this.registry           = registry;
        this.matchService        = matchService;
        this.playerStatsService  = playerStatsService;
        this.dataLoader          = dataLoader;
    }

    @PostConstruct
    public void registerMetrics() {

        // ── Métriques métier jeu (Gauge — valeur dynamique) ──────
        Gauge.builder("game_matches_total", matchService, s -> s.countTotal())
             .description("Nombre total de matchs")
             .tag("app", "groupe7")
             .register(registry);

        Gauge.builder("game_matches_active", matchService, s -> s.countActive())
             .description("Matchs en cours (IN_PROGRESS)")
             .tag("app", "groupe7")
             .register(registry);

        Gauge.builder("game_players_active", playerStatsService, s -> s.countActive())
             .description("Joueurs actifs")
             .tag("app", "groupe7")
             .register(registry);

        Gauge.builder("game_players_connected", dataLoader,
                      dl -> dl.getServers().stream().mapToInt(s -> s.getConnectedPlayers()).sum())
             .description("Joueurs connectés (somme des serveurs)")
             .tag("app", "groupe7")
             .register(registry);

        Gauge.builder("game_server_load_avg", dataLoader,
                      dl -> dl.getServers().stream()
                              .mapToDouble(s -> s.getLoad())
                              .average().orElse(0.0))
             .description("Charge moyenne des serveurs")
             .tag("app", "groupe7")
             .register(registry);

        Gauge.builder("game_match_duration_avg_seconds", matchService,
                      s -> s.averageDurationSeconds())
             .description("Durée moyenne des matchs terminés")
             .tag("app", "groupe7")
             .register(registry);

        Gauge.builder("game_player_elo_avg", playerStatsService,
                      s -> s.averageElo())
             .description("ELO moyen des joueurs")
             .tag("app", "groupe7")
             .register(registry);

        // ── Compteur de requêtes dashboard ───────────────────────
        dashboardRequestCounter = Counter.builder("game_dashboard_requests_total")
             .description("Nombre d'appels à GET /api/dashboard")
             .tag("app", "groupe7")
             .register(registry);
    }

    /** Incrémente le compteur à chaque appel dashboard */
    public void recordDashboardRequest() {
        if (dashboardRequestCounter != null) {
            dashboardRequestCounter.increment();
        }
    }

    /** Snapshot des métriques métier actuelles */
    public List<MetricSampleDto> getCurrentSamples() {
        return List.of(
            new MetricSampleDto("game_matches_total",
                matchService.countTotal(), "count", Map.of("app", "groupe7")),
            new MetricSampleDto("game_matches_active",
                matchService.countActive(), "count", Map.of("app", "groupe7")),
            new MetricSampleDto("game_players_active",
                playerStatsService.countActive(), "count", Map.of("app", "groupe7")),
            new MetricSampleDto("game_players_connected",
                dataLoader.getServers().stream().mapToInt(s -> s.getConnectedPlayers()).sum(),
                "count", Map.of("app", "groupe7")),
            new MetricSampleDto("game_server_load_avg",
                dataLoader.getServers().stream().mapToDouble(s -> s.getLoad()).average().orElse(0),
                "ratio", Map.of("app", "groupe7")),
            new MetricSampleDto("game_match_duration_avg_seconds",
                matchService.averageDurationSeconds(), "seconds", Map.of("app", "groupe7"))
        );
    }
}
