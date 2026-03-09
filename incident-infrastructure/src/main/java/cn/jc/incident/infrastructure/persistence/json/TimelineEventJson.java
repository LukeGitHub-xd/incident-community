package cn.jc.incident.infrastructure.persistence.json;

import lombok.Data;

@Data
public class TimelineEventJson {

    private String time;

    private String event;

    private String source;
    @Override
    public String toString() {
        return time + " | " + event + " | " + source;
    }
}