package com.example.monitoring.dto;

import java.time.LocalDateTime;
import java.util.List;

public class HealthStatusDto {
    private String status;           // HEALTHY, DEGRADED, DOWN
    private double uptimePercent;
    private long   totalAlerts;
    private long   openAlerts;
    private double avgLatencyMs;
    private LocalDateTime checkedAt;
    private List<String> issues;

    public HealthStatusDto() { this.checkedAt = LocalDateTime.now(); }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getUptimePercent() { return uptimePercent; }
    public void setUptimePercent(double uptimePercent) { this.uptimePercent = uptimePercent; }
    public long getTotalAlerts() { return totalAlerts; }
    public void setTotalAlerts(long totalAlerts) { this.totalAlerts = totalAlerts; }
    public long getOpenAlerts() { return openAlerts; }
    public void setOpenAlerts(long openAlerts) { this.openAlerts = openAlerts; }
    public double getAvgLatencyMs() { return avgLatencyMs; }
    public void setAvgLatencyMs(double avgLatencyMs) { this.avgLatencyMs = avgLatencyMs; }
    public LocalDateTime getCheckedAt() { return checkedAt; }
    public void setCheckedAt(LocalDateTime checkedAt) { this.checkedAt = checkedAt; }
    public List<String> getIssues() { return issues; }
    public void setIssues(List<String> issues) { this.issues = issues; }
}
