package cn.jc.incident.infrastructure.persistence.json;

import lombok.Data;

@Data
public class TrendPointJson {

    private String minute;

    private int errorCount;
    @Override
    public String toString() {
        return minute + " → errors=" + errorCount;
    }
}