package com.example;

import com.example.monitoring.service.AlertService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAlertRules(AlertService alertService) {
        return args -> {
            alertService.initDefaultRules();
            System.out.println("[Groupe 7] ==========================================");
            System.out.println("[Groupe 7] Microservice Monitoring — LoL Simulation");
            System.out.println("[Groupe 7] ==========================================");
            System.out.println("[Groupe 7] API (via Nginx) : http://localhost:8080/api/v1/dashboard");
            System.out.println("[Groupe 7] Auth            : POST http://localhost:8080/api/v1/auth/login");
            System.out.println("[Groupe 7] Health          : http://localhost:8080/api/v1/monitoring/health");
            System.out.println("[Groupe 7] Prometheus      : http://localhost:9090");
            System.out.println("[Groupe 7] Grafana         : http://localhost:3000  (admin/admin)");
            System.out.println("[Groupe 7] phpMyAdmin      : http://localhost:8081  (root/root)");
            System.out.println("[Groupe 7] 7 regles d'alerte initialisees.");
            System.out.println("[Groupe 7] ==========================================");
        };
    }
}
