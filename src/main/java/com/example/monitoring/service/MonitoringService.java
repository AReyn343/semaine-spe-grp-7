package com.example.monitoring.service;

import com.example.metier.entity.PlayerStats;
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
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    private final MeterRegistry      registry;
    private final MatchService        matchService;
    private final PlayerStatsService  playerStatsService;
    private final DataLoaderService   dataLoader;

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

        // ── Métriques globales ────────────────────────────────────
        Gauge.builder("game_matches_total", matchService, s -> s.countTotal())
             .description("Nombre total de matchs").tag("app", "groupe7").register(registry);

        Gauge.builder("game_matches_active", matchService, s -> s.countActive())
             .description("Matchs en cours").tag("app", "groupe7").register(registry);

        Gauge.builder("game_players_active", playerStatsService, s -> s.countActive())
             .description("Joueurs actifs").tag("app", "groupe7").register(registry);

        Gauge.builder("game_match_duration_avg_seconds", matchService, s -> s.averageDurationSeconds())
             .description("Durée moyenne des matchs").tag("app", "groupe7").register(registry);

        // ── Joueurs connectés par région ──────────────────────────
        for (String region : List.of("EUW", "NA", "KR")) {
            Gauge.builder("game_players_connected_by_region", dataLoader,
                    dl -> dl.getServers().stream()
                            .filter(s -> region.equals(s.getRegion()))
                            .mapToInt(s -> s.getConnectedPlayers()).sum())
                 .description("Joueurs connectés par région")
                 .tag("app", "groupe7")
                 .tag("region", region)
                 .register(registry);
        }

        // ── Matchs en cours par mode ──────────────────────────────
        for (String mode : List.of("RANKED_SOLO", "RANKED_FLEX", "NORMAL_DRAFT", "ARAM")) {
            Gauge.builder("game_matches_active_by_mode", dataLoader,
                    dl -> dl.getMatches().stream()
                            .filter(m -> "IN_PROGRESS".equals(m.getStatus()) && mode.equals(m.getMode()))
                            .count())
                 .description("Matchs actifs par mode")
                 .tag("app", "groupe7")
                 .tag("mode", mode)
                 .register(registry);
        }

        // ── KDA moyen ─────────────────────────────────────────────
        Gauge.builder("game_player_kda_avg", dataLoader,
                dl -> dl.getPlayerStats().stream()
                        .mapToDouble(p -> (p.getKills() + p.getAssists()) /
                                         (double) Math.max(1, p.getDeaths()))
                        .average().orElse(0.0))
             .description("KDA moyen des joueurs")
             .tag("app", "groupe7")
             .register(registry);

        // ── CS moyen par minute ───────────────────────────────────
        Gauge.builder("game_player_cs_per_min_avg", dataLoader,
                dl -> dl.getPlayerStats().stream()
                        .filter(p -> p.getPlayTimeSeconds() > 60)
                        .mapToDouble(p -> p.getCs() / (p.getPlayTimeSeconds() / 60.0))
                        .average().orElse(0.0))
             .description("CS moyen par minute")
             .tag("app", "groupe7")
             .register(registry);

        // ── Top champions (top 5) ─────────────────────────────────
        registerTopChampions();

        // ── Distribution des tiers ────────────────────────────────
        for (String tier : List.of("IRON", "BRONZE", "SILVER", "GOLD", "PLATINUM", "DIAMOND", "MASTER", "CHALLENGER")) {
            Gauge.builder("game_players_by_tier", dataLoader,
                    dl -> dl.getPlayerStats().stream()
                            .filter(p -> tier.equals(p.getTier()))
                            .count())
                 .description("Joueurs par tier")
                 .tag("app", "groupe7")
                 .tag("tier", tier)
                 .register(registry);
        }

        // ── Charge serveurs ───────────────────────────────────────
        Gauge.builder("game_server_load_avg", dataLoader,
                dl -> dl.getServers().stream().mapToDouble(s -> s.getLoad()).average().orElse(0.0))
             .description("Charge moyenne des serveurs").tag("app", "groupe7").register(registry);

        // ── Compteur dashboard ────────────────────────────────────
        dashboardRequestCounter = Counter.builder("game_dashboard_requests_total")
             .description("Appels à GET /api/dashboard").tag("app", "groupe7").register(registry);
    }

    private void registerTopChampions() {
        // On enregistre un Gauge par champion pour les 20 champions possibles
        for (String champion : List.of(
                "Ahri","Zed","Jinx","Thresh","Lee Sin","Darius","Lux",
                "Yasuo","Vayne","Blitzcrank","Orianna","Katarina","Ezreal",
                "Morgana","Vi","Caitlyn","Syndra","Jhin","Yone","Garen")) {
            Gauge.builder("game_champion_players", dataLoader,
                    dl -> dl.getPlayerStats().stream()
                            .filter(p -> champion.equals(p.getChampion()))
                            .count())
                 .description("Joueurs par champion")
                 .tag("app", "groupe7")
                 .tag("champion", champion)
                 .register(registry);
        }
    }

    public void recordDashboardRequest() {
        if (dashboardRequestCounter != null) dashboardRequestCounter.increment();
    }

    public List<MetricSampleDto> getCurrentSamples() {
        return List.of(
            new MetricSampleDto("game_matches_total",    matchService.countTotal(),          "count",   Map.of("app","groupe7")),
            new MetricSampleDto("game_matches_active",   matchService.countActive(),          "count",   Map.of("app","groupe7")),
            new MetricSampleDto("game_players_active",   playerStatsService.countActive(),    "count",   Map.of("app","groupe7")),
            new MetricSampleDto("game_player_kda_avg",
                dataLoader.getPlayerStats().stream()
                    .mapToDouble(p -> (p.getKills()+p.getAssists())/(double)Math.max(1,p.getDeaths()))
                    .average().orElse(0),                                                    "ratio",   Map.of("app","groupe7")),
            new MetricSampleDto("game_server_load_avg",
                dataLoader.getServers().stream().mapToDouble(s -> s.getLoad()).average().orElse(0), "ratio", Map.of("app","groupe7"))
        );
    }
}
