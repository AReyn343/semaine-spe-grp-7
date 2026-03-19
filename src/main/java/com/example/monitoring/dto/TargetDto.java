package com.example.monitoring.dto;

public class TargetDto {
    private Long id;
    private String name;
    private String type;
    private String environment;
    private String region;

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
}
