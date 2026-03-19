package com.example.monitoring.service;

import com.example.metier.service.DataLoaderService;
import com.example.metier.service.MatchService;
import com.example.monitoring.dto.AlertDto;
import com.example.monitoring.entity.AlertEvent;
import com.example.monitoring.entity.AlertRule;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Évalue les règles d'alerte sur les métriques courantes.
 * Persiste les AlertEvent en base et expose le compteur via Micrometer.
 */
@Service
public class AlertService {

    @PersistenceContext
    private EntityManager em;

    private final MeterRegistry    registry;
    private final MonitoringService monitoringService;
    private final DataLoaderService dataLoader;
    private final MatchService      matchService;

    public AlertService(MeterRegistry registry,
                        MonitoringService monitoringService,
                        DataLoaderService dataLoader,
                        MatchService matchService) {
        this.registry          = registry;
        this.monitoringService = monitoringService;
        this.dataLoader        = dataLoader;
        this.matchService      = matchService;
    }

    @PostConstruct
    public void registerAlertMetric() {
        // Gauge Micrometer — nombre d'alertes ouvertes
        Gauge.builder("game_alerts_open", this, AlertService::countOpenAlerts)
             .description("Nombre d'alertes ouvertes")
             .tag("app", "groupe7")
             .register(registry);
    }

    /** Initialise les règles d'alerte par défaut en base */
    @Transactional
    public void initDefaultRules() {
        if (em.createQuery("SELECT COUNT(r) FROM AlertRule r", Long.class)
              .getSingleResult() > 0) return;

        saveRule("server_load_high",   "game_server_load_avg",           "GT",  0.85, "WARNING",
                 "Charge serveur moyenne > 85%");
        saveRule("server_load_critical","game_server_load_avg",          "GT",  0.95, "CRITICAL",
                 "Charge serveur moyenne > 95% — risque de panne");
        saveRule("no_active_matches",   "game_matches_active",           "LT",  1.0,  "WARNING",
                 "Aucun match en cours");
        saveRule("dashboard_slow",      "http_server_requests_seconds",  "GT",  2.0,  "WARNING",
                 "Latence dashboard > 2s");
        saveRule("low_players",         "game_players_connected",        "LT",  10.0, "INFO",
                 "Moins de 10 joueurs connectés");
    }

    private void saveRule(String name, String metric, String op,
                          double threshold, String severity, String desc) {
        AlertRule rule = new AlertRule();
        rule.setMetricName(metric);
        rule.setOperator(op);
        rule.setThreshold(threshold);
        rule.setSeverity(severity);
        rule.setDescription(desc);
        rule.setEnabled(true);
        em.persist(rule);
    }

    /** Évalue toutes les règles actives et persiste les alertes déclenchées */
    @Transactional
    public List<AlertDto> evaluateRules() {
        List<AlertRule> rules = em.createQuery(
                "SELECT r FROM AlertRule r WHERE r.enabled = true", AlertRule.class)
                .getResultList();

        List<AlertDto> triggered = new ArrayList<>();

        for (AlertRule rule : rules) {
            double currentValue = resolveMetricValue(rule.getMetricName());
            if (matches(currentValue, rule.getOperator(), rule.getThreshold())) {
                AlertEvent event = new AlertEvent();
                event.setRuleName(rule.getDescription());
                event.setMetricName(rule.getMetricName());
                event.setCurrentValue(currentValue);
                event.setStatus("OPEN");
                event.setSeverity(rule.getSeverity());
                event.setMessage(String.format("[%s] %s = %.2f (seuil: %s %.2f)",
                        rule.getSeverity(), rule.getMetricName(),
                        currentValue, rule.getOperator(), rule.getThreshold()));
                event.setTriggeredAt(LocalDateTime.now());
                em.persist(event);

                triggered.add(toDto(event, rule.getThreshold()));
            }
        }
        return triggered;
    }

    /** Liste toutes les alertes ouvertes */
    @Transactional(readOnly = true)
    public List<AlertDto> getOpenAlerts() {
        return em.createQuery(
                "SELECT e FROM AlertEvent e WHERE e.status = 'OPEN' ORDER BY e.triggeredAt DESC",
                AlertEvent.class)
                .getResultList()
                .stream()
                .map(e -> toDto(e, 0))
                .collect(Collectors.toList());
    }

    /** Liste toutes les alertes */
    @Transactional(readOnly = true)
    public List<AlertDto> getAllAlerts() {
        return em.createQuery(
                "SELECT e FROM AlertEvent e ORDER BY e.triggeredAt DESC",
                AlertEvent.class)
                .setMaxResults(50)
                .getResultList()
                .stream()
                .map(e -> toDto(e, 0))
                .collect(Collectors.toList());
    }

    public long countOpenAlerts() {
        try {
            return em.createQuery(
                    "SELECT COUNT(e) FROM AlertEvent e WHERE e.status = 'OPEN'", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    // ── Helpers privés ────────────────────────────────────────────

    private double resolveMetricValue(String metricName) {
        return switch (metricName) {
            case "game_server_load_avg"  -> dataLoader.getServers().stream()
                                               .mapToDouble(s -> s.getLoad()).average().orElse(0);
            case "game_matches_active"   -> matchService.countActive();
            case "game_players_connected"-> dataLoader.getServers().stream()
                                               .mapToInt(s -> s.getConnectedPlayers()).sum();
            default -> 0.0;
        };
    }

    private boolean matches(double value, String operator, double threshold) {
        return switch (operator) {
            case "GT"  -> value > threshold;
            case "GTE" -> value >= threshold;
            case "LT"  -> value < threshold;
            case "LTE" -> value <= threshold;
            case "EQ"  -> value == threshold;
            default    -> false;
        };
    }

    private AlertDto toDto(AlertEvent e, double threshold) {
        AlertDto dto = new AlertDto();
        dto.setId(e.getId());
        dto.setRuleName(e.getRuleName());
        dto.setMetricName(e.getMetricName());
        dto.setCurrentValue(e.getCurrentValue());
        dto.setThreshold(threshold);
        dto.setSeverity(e.getSeverity());
        dto.setStatus(e.getStatus());
        dto.setMessage(e.getMessage());
        dto.setTriggeredAt(e.getTriggeredAt());
        return dto;
    }
}
