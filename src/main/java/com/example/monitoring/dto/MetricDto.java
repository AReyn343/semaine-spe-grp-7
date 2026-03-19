package com.example.monitoring.dto;

public class MetricDto {
    private String name;
    private String description;
    private String category;
    private String unit;
    private String source;

    public MetricDto() {}
    public MetricDto(String name, String description, String category, String unit, String source) {
        this.name = name; this.description = description;
        this.category = category; this.unit = unit; this.source = source;
    }

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
