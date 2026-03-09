package cn.jc.incident.core.model;

import lombok.Data;

@Data
public class TimelineEvent {

    private String time;

    private String event;

    private String source;
    @Override
    public String toString() {
        return time + " | " + event + " | " + source;
    }
}