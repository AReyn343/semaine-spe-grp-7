package com.example.monitoring.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class MetricSampleDto {
    private String metricName;
    private double value;
    private String unit;
    private LocalDateTime timestamp;
    private Map<String, String> tags;

    public MetricSampleDto() {}
    public MetricSampleDto(String metricName, double value, String unit, Map<String, String> tags) {
        this.metricName = metricName; this.value = value;
        this.unit = unit; this.tags = tags;
        this.timestamp = LocalDateTime.now();
    }

    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }
}
