package com.example.monitoring.service;

import com.example.metier.service.DataLoaderService;
import com.example.monitoring.dto.HealthStatusDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Construit une vue synthétique de santé de la plateforme.
 */
@Service
public class HealthService {

    private final DataLoaderService dataLoader;
    private final AlertService      alertService;

    public HealthService(DataLoaderService dataLoader, AlertService alertService) {
        this.dataLoader   = dataLoader;
        this.alertService = alertService;
    }

    public HealthStatusDto buildHealthStatus() {
        HealthStatusDto dto = new HealthStatusDto();

        long openAlerts  = alertService.countOpenAlerts();
        long totalAlerts = openAlerts; // simplifié pour le projet

        // Charge des serveurs
        double avgLoad = dataLoader.getServers().stream()
                .mapToDouble(s -> s.getLoad())
                .average().orElse(0.0);

        long degraded = dataLoader.getServers().stream()
                .filter(s -> "DEGRADED".equals(s.getHealthStatus()) || "DOWN".equals(s.getHealthStatus()))
                .count();

        // Issues détectées
        List<String> issues = new ArrayList<>();
        if (avgLoad > 0.85) issues.add("Charge serveur critique : " + String.format("%.0f%%", avgLoad * 100));
        if (degraded > 0)   issues.add(degraded + " serveur(s) dégradé(s) ou hors ligne");
        if (openAlerts > 0) issues.add(openAlerts + " alerte(s) ouverte(s)");

        // Statut global
        String status;
        if (degraded > 0 || avgLoad > 0.95) status = "DOWN";
        else if (!issues.isEmpty())          status = "DEGRADED";
        else                                 status = "HEALTHY";

        dto.setStatus(status);
        dto.setUptimePercent(status.equals("HEALTHY") ? 100.0 : status.equals("DEGRADED") ? 85.0 : 0.0);
        dto.setTotalAlerts(totalAlerts);
        dto.setOpenAlerts(openAlerts);
        dto.setAvgLatencyMs(avgLoad * 200); // simulation : charge → latence estimée
        dto.setIssues(issues);

        return dto;
    }
}
