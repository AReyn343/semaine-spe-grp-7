package com.example.monitoring.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "metric_definition")
public class MetricDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;         // ex: dashboard_latency_seconds

    @Column
    private String description;

    @Column
    private String category;     // TECHNICAL, BUSINESS

    @Column
    private String unit;         // ms, %, count, seconds

    @Column
    private String source;       // jeu, système, applicatif

    public MetricDefinition() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
