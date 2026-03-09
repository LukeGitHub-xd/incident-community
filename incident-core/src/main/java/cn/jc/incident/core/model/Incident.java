package cn.jc.incident.core.model;

import cn.jc.incident.core.dto.request.CreateIncidentRequest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Incident {

    // ===== 1. 事故事实（永远不变） =====
    private String id;
    private String serviceName;
    private String env;
    private LocalDateTime occurTime;
    private String changeSummary;
    private String rawLog;

    // ===== 2. AI 分析事实（证据） =====
    private String summary;
    private List<String> confirmedRootCauses = new ArrayList<>();
    private List<String> suspectedRootCauses = new ArrayList<>();
    private List<String> uncertainties = new ArrayList<>();
    private List<String> recommendations = new ArrayList<>();

    // ===== 3. Severity 决策事实（重点） =====
    private SeverityLevel severityLevel;
    private List<SeverityDecision> severityDecisions = new ArrayList<>();
    private boolean userImpact;

    // ===== 4. 成本 / 治理 =====
    private int tokenUsage;

    // ===== 5. 根因分析结果 =====
    private String errorLocation;   // 堆栈顶层
    private String evidence;

    // ===== 6. 报告渲染相关（补充，解决“暂无”报错） =====
    private List<TimelineEvent> timeline = new ArrayList<>();
    private List<TrendPoint> errorTrend = new ArrayList<>();
    private List<ServiceImpact> serviceImpact = new ArrayList<>();
    private List<String> immediateActions = new ArrayList<>();
    private List<String> shortTermImprovements = new ArrayList<>();
    private List<String> longTermImprovements = new ArrayList<>();

    // ===== 构建方法 =====
    public static Incident create(CreateIncidentRequest req) {
        LocalDateTime ts = req.getOccurTime() != null
                ? LocalDateTime.parse(req.getOccurTime())
                : LocalDateTime.now();
        return new Incident(
                UUID.randomUUID().toString(),
                req.getServiceName(),
                req.getEnv(),
                ts,
                req.getChangeSummary(),
                req.getLogContent(),
                null,                     // summary
                new ArrayList<>(),        // confirmedRootCauses
                new ArrayList<>(),        // suspectedRootCauses
                new ArrayList<>(),        // uncertainties
                new ArrayList<>(),        // recommendations
                null,                     // severityLevel
                new ArrayList<>(),        // severityDecisions
                false,                    // userImpact
                0,                        // tokenUsage
                null,                     // errorLocation
                null,                     // evidence
                new ArrayList<>(),        // timeline
                new ArrayList<>(),        // errorTrend
                new ArrayList<>(),        // serviceImpact
                new ArrayList<>(),        // immediateActions
                new ArrayList<>(),        // shortTermImprovements
                new ArrayList<>()         // longTermImprovements
        );
    }

    // ===== 应用 AI 分析 =====
    // ===== 应用 AI 分析 =====
    public void applyAnalysis(AIAnalysisResult aiResult) {

        if (aiResult == null) {
            return;
        }

        this.summary = aiResult.getSummary();

        if (aiResult.getConfirmedFindings() != null) {
            this.confirmedRootCauses = aiResult.getConfirmedFindings()
                    .stream()
                    .map(ConfirmedFinding::getDescription)
                    .toList();
        }

        if (aiResult.getSuspectedFindings() != null) {
            this.suspectedRootCauses = aiResult.getSuspectedFindings()
                    .stream()
                    .map(SuspectedFinding::getDescription)
                    .toList();
        }

        if (aiResult.getUncertainties() != null) {
            this.uncertainties = new ArrayList<>(aiResult.getUncertainties());
        }

        if (aiResult.getRecommendedNextSteps() != null) {
            this.recommendations = new ArrayList<>(aiResult.getRecommendedNextSteps());
        }

        /**
         * ⚠️ 关键修改：
         * AI 不再直接决定 userImpact
         *
         * 只有 P0 才认为系统级影响
         */
        if ("P0".equals(aiResult.getSuggestedSeverity())) {
            this.userImpact = true;
        } else {
            this.userImpact = false;
        }
    }

    // ===== 应用 Severity 决策 =====
    public void applySeverity(SeverityScore score) {
        if (score == null) return;

        if (score.getDecisions() != null) {
            this.severityDecisions = new ArrayList<>(score.getDecisions());
        }

        this.severityLevel = score.getFinalLevel();
    }
}