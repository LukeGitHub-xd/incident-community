package cn.jc.incident.core.evaluation;

import cn.jc.incident.core.model.SeverityLevel;

public class AISeverityScorer {

    public SeverityLevel parse(String aiSeverity) {
        try {
            return SeverityLevel.valueOf(aiSeverity);
        } catch (Exception e) {
            return null;
        }
    }
}
