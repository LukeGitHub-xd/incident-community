package cn.jc.incident.core.report.renderer;

import cn.jc.incident.core.dto.view.IncidentReportView;
import org.springframework.stereotype.Component;
@Component
public class MarkdownReportRenderer {

    public String render(IncidentReportView view) {
        StringBuilder md = new StringBuilder();

        // ======================
        // Title
        // ======================
        md.append("# 事故分析报告\n\n");

        // ======================
        // Executive Summary
        // ======================
        md.append("## 一、事件概览\n");
        md.append("- **严重级别**：").append(view.getExecutiveSummary().getSeverity()).append("\n");
        md.append("- **是否影响用户**：")
                .append(view.getExecutiveSummary().isUserImpact() ? "是" : "否")
                .append("\n");
        md.append("- **建议动作**：")
                .append(view.getExecutiveSummary().getRecommendedAction())
                .append("\n\n");

        md.append("> ")
                .append(view.getExecutiveSummary().getHeadline())
                .append("\n\n");

        // ======================
        // Impact
        // ======================
        md.append("## 二、影响评估\n");
        md.append("- 服务：").append(view.getImpact().getServiceName()).append("\n");
        md.append("- 环境：").append(view.getImpact().getEnv()).append("\n");
        md.append("- 时间：").append(view.getImpact().getOccurTime()).append("\n");
        md.append("- 影响范围：").append(view.getImpact().getScope()).append("\n\n");

        // ======================
        // Root Cause
        // ======================
        md.append("## 三、根因分析\n");

        if (!view.getRootCause().getConfirmedRootCauses().isEmpty()) {
            md.append("### ✅ 已确认根因\n");
            view.getRootCause().getConfirmedRootCauses()
                    .forEach(c -> md.append("- ").append(c).append("\n"));
        }

        if (!view.getRootCause().getSuspectedRootCauses().isEmpty()) {
            md.append("\n### ⚠️ 推测根因\n");
            view.getRootCause().getSuspectedRootCauses()
                    .forEach(c -> md.append("- ").append(c).append("\n"));
        }

        if (!view.getRootCause().getUncertainties().isEmpty()) {
            md.append("\n### ❓ 不确定因素\n");
            view.getRootCause().getUncertainties()
                    .forEach(c -> md.append("- ").append(c).append("\n"));
        }

        md.append("\n");

        // ======================
        // Key Signals
        // ======================
        md.append("## 四、关键信号\n");
        view.getKeySignals().forEach(s -> {
            md.append("- **").append(s.getSource()).append("**：")
                    .append(s.getDescription()).append("\n");
        });

        // ======================
        // Action Plan
        // ======================
        md.append("\n## 五、行动建议\n");

        md.append("### 立即行动\n");
        view.getActionPlan().getImmediateActions()
                .forEach(a -> md.append("- ").append(a).append("\n"));

        md.append("\n### 短期改进\n");
        view.getActionPlan().getShortTermImprovements()
                .forEach(a -> md.append("- ").append(a).append("\n"));

        md.append("\n### 长期改进\n");
        view.getActionPlan().getLongTermImprovements()
                .forEach(a -> md.append("- ").append(a).append("\n"));

        // ======================
        // Confidence
        // ======================
        md.append("\n## 六、可信度说明\n");
        md.append("- 可信度评分：")
                .append(view.getConfidence().getConfidenceScore())
                .append("\n");
// ======================
// Timeline
// ======================
        md.append("\n## 七、事故时间线\n");

        if (view.getTimeline() == null || view.getTimeline().isEmpty()) {

            md.append("- 暂无\n");

        } else {

            view.getTimeline().forEach(t ->
                    md.append("- ")
                            .append(t.getTime())
                            .append(" | ")
                            .append(t.getEvent())
                            .append("\n")
            );
        }

// ======================
// Error Trend
// ======================
        md.append("\n## 八、异常趋势\n");

        if (view.getErrorTrend() == null || view.getErrorTrend().isEmpty()) {

            md.append("- 暂无\n");

        } else {

            view.getErrorTrend().stream().distinct().forEach(p ->
                    md.append("- ")
                            .append(p.getMinute())
                            .append(" → errors=")
                            .append(p.getErrorCount())
                            .append("\n")
            );
        }

// ======================
// Service Impact
// ======================
        md.append("\n## 九、服务影响热力\n");

        if (view.getServiceImpact() == null || view.getServiceImpact().isEmpty()) {

            md.append("- 暂无\n");

        } else {

            view.getServiceImpact().forEach(s ->
                    md.append("- ")
                            .append(s.getService())
                            .append(" | impact=")
                            .append(s.getLevel())
                            .append("\n")
            );
        }
        return md.toString();

    }
}

