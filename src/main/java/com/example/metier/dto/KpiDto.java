package com.example.metier.dto;

public class KpiDto {
    private String label;
    private Object value;
    private String unit;

    public KpiDto() {}

    public KpiDto(String label, Object value, String unit) {
        this.label = label;
        this.value = value;
        this.unit  = unit;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}