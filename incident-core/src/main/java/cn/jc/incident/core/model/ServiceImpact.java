package cn.jc.incident.core.model;

import lombok.Data;

@Data
public class ServiceImpact {

    private String service;

    private String level; // HIGH / MEDIUM / LOW
    @Override
    public String toString() {
        return service + " | impact=" + level;
    }
}