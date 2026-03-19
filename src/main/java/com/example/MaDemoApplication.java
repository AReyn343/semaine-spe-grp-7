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

    /**
     * Initialise les règles d'alerte par défaut au démarrage.
     */
    @Bean
    public CommandLineRunner initAlertRules(AlertService alertService) {
        return args -> {
            alertService.initDefaultRules();
            System.out.println("[Groupe 7] Règles d'alerte initialisées.");
            System.out.println("[Groupe 7] Dashboard : http://localhost:8080/api/dashboard");
            System.out.println("[Groupe 7] Monitoring : http://localhost:8080/api/monitoring/health");
            System.out.println("[Groupe 7] Prometheus : http://localhost:8080/actuator/prometheus");
            System.out.println("[Groupe 7] Grafana    : http://localhost:3000");
        };
    }
}
