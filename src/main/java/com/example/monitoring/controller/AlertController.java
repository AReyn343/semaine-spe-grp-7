package com.example.monitoring.controller;

import com.example.monitoring.dto.AlertDto;
import com.example.monitoring.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * GET /api/monitoring/alerts
     * Toutes les alertes (50 dernières).
     */
    @GetMapping
    public List<AlertDto> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    /**
     * GET /api/monitoring/alerts/open
     * Alertes actuellement ouvertes.
     */
    @GetMapping("/open")
    public List<AlertDto> getOpenAlerts() {
        return alertService.getOpenAlerts();
    }

    /**
     * POST /api/monitoring/alerts/evaluate
     * Déclenche manuellement l'évaluation des règles.
     */
    @PostMapping("/evaluate")
    public ResponseEntity<List<AlertDto>> evaluate() {
        List<AlertDto> triggered = alertService.evaluateRules();
        return ResponseEntity.ok(triggered);
    }
}
