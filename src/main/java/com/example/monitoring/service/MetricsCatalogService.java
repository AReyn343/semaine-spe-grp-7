package com.example.monitoring.service;

import com.example.monitoring.dto.MetricDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Catalogue de toutes les métriques suivies par le microservice.
 * Sert à documenter et exposer la liste via GET /monitoring/metrics/catalog.
 */
@Service
public class MetricsCatalogService {

    public List<MetricDto> getCatalog() {
        return List.of(
            // ── Métriques techniques ──────────────────────────────
            new MetricDto(
                "http_server_requests_seconds",
                "Latence des requêtes HTTP (histogramme)",
                "TECHNICAL", "seconds", "spring-actuator"
            ),
            new MetricDto(
                "http_server_requests_seconds_count",
                "Nombre total de requêtes HTTP reçues",
                "TECHNICAL", "count", "spring-actuator"
            ),
            new MetricDto(
                "jvm_memory_used_bytes",
                "Mémoire JVM utilisée (heap + non-heap)",
                "TECHNICAL", "bytes", "jvm"
            ),
            new MetricDto(
                "process_cpu_usage",
                "Utilisation CPU du processus Spring Boot",
                "TECHNICAL", "ratio", "jvm"
            ),
            new MetricDto(
                "hikaricp_connections_active",
                "Connexions actives dans le pool HikariCP",
                "TECHNICAL", "count", "hikari"
            ),
            new MetricDto(
                "hikaricp_connections_pending",
                "Connexions en attente dans le pool HikariCP",
                "TECHNICAL", "count", "hikari"
            ),
            new MetricDto(
                "jvm_threads_live_threads",
                "Nombre de threads actifs dans la JVM",
                "TECHNICAL", "count", "jvm"
            ),
            // ── Métriques métier jeu ──────────────────────────────
            new MetricDto(
                "game_matches_total",
                "Nombre total de matchs chargés",
                "BUSINESS", "count", "metier"
            ),
            new MetricDto(
                "game_matches_active",
                "Nombre de matchs en cours (IN_PROGRESS)",
                "BUSINESS", "count", "metier"
            ),
            new MetricDto(
                "game_players_active",
                "Nombre de joueurs actifs (ayant joué au moins 1 match)",
                "BUSINESS", "count", "metier"
            ),
            new MetricDto(
                "game_players_connected",
                "Joueurs connectés en temps réel (somme serveurs)",
                "BUSINESS", "count", "metier"
            ),
            new MetricDto(
                "game_server_load_avg",
                "Charge moyenne de tous les serveurs de jeu",
                "BUSINESS", "ratio", "metier"
            ),
            new MetricDto(
                "game_match_duration_avg_seconds",
                "Durée moyenne des matchs terminés",
                "BUSINESS", "seconds", "metier"
            ),
            new MetricDto(
                "game_alerts_open",
                "Nombre d'alertes actuellement ouvertes",
                "BUSINESS", "count", "monitoring"
            )
        );
    }
}
