package com.example.metier.controller;

import com.example.metier.dto.DashboardDto;
import com.example.metier.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /** GET /api/dashboard — vue complète de la plateforme */
    @GetMapping
    public DashboardDto getDashboard() {
        return dashboardService.buildDashboard();
    }
}
