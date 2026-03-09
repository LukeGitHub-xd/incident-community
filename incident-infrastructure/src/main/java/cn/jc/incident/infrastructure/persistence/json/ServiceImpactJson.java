package cn.jc.incident.infrastructure.persistence.json;

import lombok.Data;

@Data
public class ServiceImpactJson {

    private String service;

    private String level; // HIGH / MEDIUM / LOW
    @Override
    public String toString() {
        return service + " | impact=" + level;
    }
}