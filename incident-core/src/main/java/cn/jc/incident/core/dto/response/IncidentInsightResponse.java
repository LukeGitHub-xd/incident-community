package cn.jc.incident.core.dto.response;

import cn.jc.incident.core.model.ActionLevel;
import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.model.InsightCapabilities;
import cn.jc.incident.core.model.SeverityLevel;
import lombok.Data;

import java.util.List;

@Data
public class IncidentInsightResponse {

    private String incidentId;

    private SeverityLevel severity;
    private ActionLevel action;
    private double confidence;

    private List<String> why;
    private List<String> topSignals;
    private List<String> nextActions;

    private InsightCapabilities capabilities;

    public static IncidentInsightResponse from(
            String incidentId,
            IncidentInsight insight
    ) {
        IncidentInsightResponse resp = new IncidentInsightResponse();
        resp.setIncidentId(incidentId);
        resp.setSeverity(insight.getSeverity());
        resp.setAction(insight.getAction());
        resp.setConfidence(insight.getConfidence());
        resp.setWhy(insight.getWhy());
        resp.setTopSignals(insight.getTopSignals());
        resp.setNextActions(insight.getNextActions());
        resp.setCapabilities(insight.getCapabilities());
        return resp;
    }
}
