package cn.jc.incident.core.dto.response;

import cn.jc.incident.core.model.Incident;
import cn.jc.incident.core.model.SeverityDecisionFormatter;
import cn.jc.incident.core.model.SeverityDecisionView;
import cn.jc.incident.core.model.SeverityLevel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidentResponse {

    private String incidentId;
    private String serviceName;
    private String env;
    private LocalDateTime occurTime;

    private String summary;

    private List<String> confirmedRootCauses;
    private List<String> suspectedRootCauses;
    private List<String> uncertainties;
    private List<String> recommendations;

    private SeverityLevel severityLevel;
    private boolean userImpact;

    /** 结构化决策视图 */
    private List<SeverityDecisionView> severityDecisionViews;

    /** 简单可读原因（快速展示） */
    private List<String> severityReasons;

    private int tokenUsage;

    public static IncidentResponse fromIncident(Incident incident) {
        IncidentResponse res = new IncidentResponse();
        res.setIncidentId(incident.getId());
        res.setServiceName(incident.getServiceName());
        res.setEnv(incident.getEnv());
        res.setOccurTime(incident.getOccurTime());
        res.setSummary(incident.getSummary());
        res.setConfirmedRootCauses(incident.getConfirmedRootCauses());
        res.setSuspectedRootCauses(incident.getSuspectedRootCauses());
        res.setUncertainties(incident.getUncertainties());
        res.setRecommendations(incident.getRecommendations());
        res.setSeverityLevel(incident.getSeverityLevel());
        res.setUserImpact(incident.isUserImpact());
        res.setTokenUsage(incident.getTokenUsage());

        // Explainability
        res.setSeverityDecisionViews(
                incident.getSeverityDecisions().stream()
                        .map(d -> new SeverityDecisionView(
                                d.getSource().name(),
                                d.getScorer(),
                                d.getLevel(),
                                d.getReason(),
                                d.getWeight() > 800 ? "HIGH" : d.getWeight() > 400 ? "MEDIUM" : "LOW"
                        ))
                        .toList()
        );
        res.setSeverityReasons(
                incident.getSeverityDecisions().stream()
                        .map(SeverityDecisionFormatter::format)
                        .toList()
        );
        return res;
    }
}
