package cn.jc.incident.core.report.renderer;

import cn.hutool.core.collection.CollUtil;
import cn.jc.incident.core.dto.response.ExecutiveSummary;
import cn.jc.incident.core.dto.view.*;
import cn.jc.incident.core.model.*;
import cn.jc.incident.core.plan.ActionPlan;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class WechatMarkdownRenderer {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String render(IncidentReportRenderModel model) {

        StringBuilder sb = new StringBuilder();

        IncidentReportView report = model.getReport();
        ExecutiveSummary exec = report.getExecutiveSummary();
        ImpactOverview impact = report.getImpact();
        RootCauseOverview root = report.getRootCause();
        ActionPlan plan = report.getActionPlan();
        ConfidenceView confidence = report.getConfidence();

        sb.append("# 🚨 事故分析报告\n\n");

        // 基本信息
        sb.append("> **服务**：").append(nullSafe(impact.getServiceName())).append("  \n")
                .append("> **环境**：").append(nullSafe(impact.getEnv())).append("  \n")
                .append("> **发生时间**：").append(formatTime(impact.getOccurTime())).append("  \n")
                .append("> **报告ID**：").append(model.getIncidentId()).append("  \n")
                .append("> **生成时间**：").append(formatTime(model.getGeneratedAt())).append("\n\n");

        sb.append("---\n\n");

        // 执行摘要
        sb.append("## 🧭 一、执行摘要\n\n");

        sb.append("| 项目 | 内容 |\n");
        sb.append("|------|------|\n");
        sb.append("| 严重等级 | ")
                .append(colorSeverity(exec.getSeverity()))
                .append(" |\n");

        sb.append("| 用户影响 | ")
                .append(exec.isUserImpact() ? "是" : "否")
                .append(" |\n");

        sb.append("| 建议动作 | ")
                .append(nullSafe(exec.getRecommendedActionStr()))
                .append(" |\n");

        sb.append("| 当前状态 | ")
                .append(nullSafe(exec.getStatusStr()))
                .append(" |\n\n");

        sb.append("> ").append(nullSafe(exec.getHeadline())).append("\n\n");

        sb.append("---\n\n");

        // 根因
        appendListSection(sb, "🔎 二、已确认根因",
                root.getConfirmedRootCauses());

        appendListSection(sb, "🧪 三、疑似根因",
                root.getSuspectedRootCauses());

        appendListSection(sb, "❓ 四、不确定因素",
                root.getUncertainties());

        // 行动计划
        appendListSection(sb, "🛠 五、立即处理",
                plan.getImmediateActions());

        appendListSection(sb, "📅 六、短期改进",
                plan.getShortTermImprovements());

        appendListSection(sb, "🏗 七、长期优化",
                plan.getLongTermImprovements());

        if (plan.isRecommendPostmortem()) {
            sb.append("> ⚠ 建议升级为事故复盘\n\n");
        }

        sb.append("---\n\n");

        // 关键证据
        sb.append("## 📊 八、关键证据\n\n");

        if (report.getKeySignals() == null || report.getKeySignals().isEmpty()) {
            sb.append("- 暂无\n\n");
        } else {

            sb.append("| 来源 | 重要性 | 描述 |\n");
            sb.append("|------|--------|------|\n");

            for (KeySignalView signal : report.getKeySignals()) {

                sb.append("| ")
                        .append(nullSafe(signal.getSource()))
                        .append(" | ")
                        .append(nullSafe(signal.getImportance()))
                        .append(" | ")
                        .append(nullSafe(signal.getDescription()))
                        .append(" |\n");
            }

            sb.append("\n");
        }

        sb.append("---\n\n");

        // 置信度
        sb.append("## 📈 九、AI 置信度\n\n");

        sb.append("**信心评分**：")
                .append(Math.round(confidence.getConfidenceScore() * 100))
                .append("%（")
                .append(nullSafe(confidence.getConfidenceLevel()))
                .append("）  \n\n");

        sb.append("**支撑因素：**\n");
        appendRawListOrDefault(sb, confidence.getConfidenceFactors());

        sb.append("\n**风险提示：**\n");
        appendRawListOrDefault(sb, confidence.getCaveats());

        // =============================
        // 十、事故时间线
        // =============================
        appendTimelineSection(sb,
                report.getTimeline());

        // =============================
        // 十一、异常趋势
        // =============================
        appendTrendSection(sb,
                report.getErrorTrend());

        // =============================
        // 十二、服务影响
        // =============================
        appendServiceImpactSection(sb,
                report.getServiceImpact());

        sb.append("> 本报告由 AI 事故分析引擎自动生成\n");

        return sb.toString();
    }


    private void appendRawListOrDefault(StringBuilder sb, List<String> items) {

        if (items == null || items.isEmpty()) {
            sb.append("- 暂无\n");
            return;
        }
        if (CollUtil.isNotEmpty(items)) {
            for (String item : items.stream().distinct().toList()) {
                sb.append("- ").append(item).append("\n");
            }
        }
    }

    private String colorSeverity(SeverityLevel severity) {

        if (severity == null) return "未知";

        return switch (severity) {
            case P0 -> "<font color=\"warning\">P0</font>";
            case P1 -> "<font color=\"warning\">P1</font>";
            case P2 -> "<font color=\"info\">P2</font>";
            case P3 -> "<font color=\"comment\">P3</font>";
        };
    }

    private String formatTime(LocalDateTime time) {
        if (time == null) return "-";
        return time.format(FORMATTER);
    }

    private String nullSafe(Object obj) {
        return obj == null ? "-" : obj.toString();
    }
    private void appendListSection(StringBuilder sb,
                                   String title,
                                   List<String> items) {

        sb.append("## ").append(title).append("\n\n");

        if (items == null || items.isEmpty()) {
            // ⭐️ 最小改动：默认填充“暂无”，不改架构
            sb.append("- 暂无\n\n");
        } else {
            for (String item : items) {
                sb.append("- ").append(item).append("\n");
            }
            sb.append("\n");
        }

        sb.append("---\n\n");
    }

    private void appendTimelineSection(StringBuilder sb,
                                       List<TimelineEvent> events) {

        sb.append("## 🕒 十、事故时间线\n\n");

        if (events == null || events.isEmpty()) {
            sb.append("- 暂无\n\n");
        } else {
            sb.append("| 时间 | 事件 | 来源 |\n");
            sb.append("|------|------|------|\n");
            for (TimelineEvent event : events) {
                sb.append("| ")
                        .append(nullSafe(event.getTime()))
                        .append(" | ")
                        .append(nullSafe(event.getEvent()))
                        .append(" | ")
                        .append(nullSafe(event.getSource()))
                        .append(" |\n");
            }
            sb.append("\n");
        }

        sb.append("---\n\n");
    }

    private void appendTrendSection(StringBuilder sb,
                                    List<TrendPoint> trend) {

        sb.append("## 📉 十一、异常趋势\n\n");

        if (trend == null || trend.isEmpty()) {
            sb.append("- 暂无\n\n");
        } else {
            sb.append("| 时间 | 错误数 |\n");
            sb.append("|------|--------|\n");
            for (TrendPoint point : trend) {
                sb.append("| ")
                        .append(nullSafe(point.getMinute()))
                        .append(" | ")
                        .append(point.getErrorCount())
                        .append(" |\n");
            }
            sb.append("\n");
        }

        sb.append("---\n\n");
    }

    private void appendServiceImpactSection(StringBuilder sb,
                                            List<ServiceImpact> impacts) {

        sb.append("## 🔥 十二、服务影响热力\n\n");

        if (impacts == null || impacts.isEmpty()) {
            sb.append("- 暂无\n\n");
        } else {
            sb.append("| 服务 | 影响程度 |\n");
            sb.append("|------|----------|\n");
            for (ServiceImpact impact : impacts) {
                sb.append("| ")
                        .append(nullSafe(impact.getService()))
                        .append(" | ");

                String level = impact.getLevel();
                if ("HIGH".equals(level)) {
                    sb.append("<font color=\"warning\">高</font>");
                } else if ("MEDIUM".equals(level)) {
                    sb.append("<font color=\"info\">中</font>");
                } else {
                    sb.append("<font color=\"comment\">低</font>");
                }
                sb.append(" |\n");
            }
            sb.append("\n");
        }

        sb.append("---\n\n");
    }
}