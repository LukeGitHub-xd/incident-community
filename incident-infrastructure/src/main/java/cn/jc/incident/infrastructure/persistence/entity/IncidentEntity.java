package cn.jc.incident.infrastructure.persistence.entity;

import cn.jc.incident.infrastructure.persistence.json.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "incident")
@Getter
@Setter
public class IncidentEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(name = "service_name", length = 100)
    private String serviceName;

    @Column(length = 50)
    private String env;

    @Column(name = "occur_time")
    private LocalDateTime occurTime;

    @Column(name = "change_summary", columnDefinition = "text")
    private String changeSummary;

    @Column(name = "raw_log", columnDefinition = "text")
    private String rawLog;

    @Column(columnDefinition = "text")
    private String summary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "confirmed_root_causes", columnDefinition = "jsonb")
    private List<ConfirmedFindingJson> confirmedRootCauses;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "suspected_root_causes", columnDefinition = "jsonb")
    private List<ConfirmedFindingJson> suspectedRootCauses;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "uncertainties", columnDefinition = "jsonb")
    private List<String> uncertainties;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recommendations", columnDefinition = "jsonb")
    private List<String> recommendations;


    @Column(name = "severity_level", length = 20)
    private String severityLevel;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "severity_decisions", columnDefinition = "jsonb")
    private SeverityDecisionJson severityDecisions;

    @Column(name = "user_impact")
    private Boolean userImpact;

    @Column(name = "token_usage")
    private Integer tokenUsage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "timeline", columnDefinition = "jsonb")
    private List<TimelineEventJson> timeline;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "error_trend", columnDefinition = "jsonb")
    private List<TrendPointJson> errorTrend;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "service_impact", columnDefinition = "jsonb")
    private List<ServiceImpactJson> serviceImpact;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "immediate_actions", columnDefinition = "jsonb")
    private List<String> immediateActions;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "short_term_improvements", columnDefinition = "jsonb")
    private List<String> shortTermImprovements;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "long_term_improvements", columnDefinition = "jsonb")
    private List<String> longTermImprovements;
}