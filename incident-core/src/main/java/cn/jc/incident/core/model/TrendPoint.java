package cn.jc.incident.core.model;

import lombok.Data;

@Data
public class TrendPoint {

    private String minute;

    private int errorCount;
    @Override
    public String toString() {
        return minute + " → errors=" + errorCount;
    }
}