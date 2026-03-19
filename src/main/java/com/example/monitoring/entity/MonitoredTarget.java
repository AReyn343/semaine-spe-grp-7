package com.example.monitoring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitored_target")
public class MonitoredTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;         // ex: backend-jeu-prod-eu

    @Column(nullable = false)
    private String type;         // API, BATCH, GAME_SERVER, DATABASE

    @Column
    private String environment;  // prod, staging, dev

    @Column
    private String region;       // EU, NA, ASIA

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public MonitoredTarget() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
