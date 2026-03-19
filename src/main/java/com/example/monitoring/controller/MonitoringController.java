package com.example.monitoring.controller;

import com.example.monitoring.dto.HealthStatusDto;
import com.example.monitoring.dto.MetricDto;
import com.example.monitoring.dto.MetricSampleDto;
import com.example.monitoring.dto.TargetDto;
import com.example.monitoring.service.HealthService;
import com.example.monitoring.service.MetricsCatalogService;
import com.example.monitoring.service.MonitoringService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final HealthService         healthService;
    private final MetricsCatalogService catalogService;
    private final MonitoringService     monitoringService;

    public MonitoringController(HealthService healthService,
                                MetricsCatalogService catalogService,
                                MonitoringService monitoringService) {
        this.healthService     = healthService;
        this.catalogService    = catalogService;
        this.monitoringService = monitoringService;
    }

    /**
     * GET /api/monitoring/health
     * Vue synthétique de la santé de la plateforme.
     */
    @GetMapping("/health")
    public HealthStatusDto getHealth() {
        return healthService.buildHealthStatus();
    }

    /**
     * GET /api/monitoring/metrics/catalog
     * Liste de toutes les métriques suivies.
     */
    @GetMapping("/metrics/catalog")
    public List<MetricDto> getCatalog() {
        return catalogService.getCatalog();
    }

    /**
     * GET /api/monitoring/metrics/current
     * Snapshot des valeurs actuelles des métriques métier.
     */
    @GetMapping("/metrics/current")
    public List<MetricSampleDto> getCurrentMetrics() {
        return monitoringService.getCurrentSamples();
    }

    /**
     * GET /api/monitoring/targets
     * Cibles surveillées (statique pour l'instant).
     */
    @GetMapping("/targets")
    public List<TargetDto> getTargets() {
        TargetDto target = new TargetDto();
        target.setId(1L);
        target.setName("backend-jeu-prod-eu");
        target.setType("GAME_SERVER");
        target.setEnvironment("prod");
        target.setRegion("EU");
        return List.of(target);
    }
}
