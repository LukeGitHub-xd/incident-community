package cn.jc.incident.core.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AlertManagerPayload {

    private String receiver;
    private String status;

    private Map<String, String> commonLabels;
    private Map<String, String> commonAnnotations;

    private List<Alert> alerts;

    @Data
    public static class Alert {
        private String status;
        private Map<String, String> labels;
        private Map<String, String> annotations;
        private String startsAt;
        private String endsAt;
    }
}
