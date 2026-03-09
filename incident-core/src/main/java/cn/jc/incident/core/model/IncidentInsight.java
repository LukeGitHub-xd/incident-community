package cn.jc.incident.core.model;

import lombok.Data;

import java.util.List;

@Data
public class IncidentInsight {
    private String incidentId;
    /** 最终严重级别 */
    private SeverityLevel severity;

    /** 用户现在应该做什么 */
    private ActionLevel action;
    private String actionStr;

    /** AI 对结论的信心 0.0 ~ 1.0 */
    private double confidence;

    /** 为什么是这个结论（人话，3~5 条） */
    private List<String> why;

    /** 最关键的信号（给技术负责人） */
    private List<String> topSignals;

    /** 建议的下一步行动 */
    private List<String> nextActions;

    /** 已确认根因 */
    private List<String> confirmedRootCauses;

    /** 推测中的根因 */
    private List<String> suspectedRootCauses;

    /** 当前不确定点 */
    private List<String> uncertainties;

    /** 付费能力标识（未来用） */
    private InsightCapabilities capabilities;

    private List<TimelineEvent> timeline;

    private List<TrendPoint> errorTrend;

    private List<ServiceImpact> serviceImpact;

    private List<String> shortTermImprovements;

    private List<String> longTermImprovements;
}
