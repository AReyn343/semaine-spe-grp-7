package com.example.monitoring.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "alert_rule")
public class AlertRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_name", nullable = false)
    private String metricName;   // ex: dashboard_latency_seconds

    @Column(nullable = false)
    private String operator;     // GT, LT, GTE, LTE, EQ

    @Column(nullable = false)
    private double threshold;

    @Column(nullable = false)
    private String severity;     // INFO, WARNING, CRITICAL

    @Column(nullable = false)
    private boolean enabled = true;

    @Column
    private String description;

    public AlertRule() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public double getThreshold() { return threshold; }
    public void setThreshold(double threshold) { this.threshold = threshold; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
