package cn.jc.incident.core.dto.assembler;

import cn.jc.incident.core.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class IncidentInsightAssembler {

    public IncidentInsight assemble(Incident incident) {

        IncidentInsight insight = new IncidentInsight();

        insight.setIncidentId(incident.getId());
        insight.setSeverity(incident.getSeverityLevel());

        insight.setAction(resolveAction(incident));
        insight.setActionStr(insight.getAction().getZh());

        insight.setConfidence(calculateConfidence(incident));

        insight.setWhy(buildWhy(incident));

        insight.setTopSignals(extractTopSignals(incident));

        insight.setNextActions(buildNextActions(incident));

        insight.setCapabilities(resolveCapabilities());

        // ⭐⭐⭐ 新增内容（核心）

        insight.setConfirmedRootCauses(
                incident.getConfirmedRootCauses()
        );

        insight.setSuspectedRootCauses(
                incident.getSuspectedRootCauses()
        );

        insight.setUncertainties(
                incident.getUncertainties()
        );

        insight.setTimeline(
                incident.getTimeline()
        );

        insight.setErrorTrend(
                incident.getErrorTrend()
        );

        insight.setServiceImpact(
                incident.getServiceImpact()
        );

        insight.setShortTermImprovements(
                incident.getShortTermImprovements()
        );

        insight.setLongTermImprovements(
                incident.getLongTermImprovements()
        );
        insight.setTimeline(incident.getTimeline());
        insight.setErrorTrend(incident.getErrorTrend());
        insight.setServiceImpact(incident.getServiceImpact());
        insight.setShortTermImprovements(incident.getShortTermImprovements());
        insight.setLongTermImprovements(incident.getLongTermImprovements());
        return insight;
    }
    // ===============================
    // 1️⃣ Action = 给“人”的指令
    // ===============================
    private ActionLevel resolveAction(Incident incident) {

        // 1️⃣ 只要确认用户影响 → 立刻处理
        if (incident.isUserImpact()) {
            return ActionLevel.IMMEDIATE;
        }

        // 2️⃣ 高严重级别 → 需要调查
        if (incident.getSeverityLevel() == SeverityLevel.P1
                || incident.getSeverityLevel() == SeverityLevel.P2) {
            return ActionLevel.INVESTIGATE;
        }

        // 3️⃣ 其余情况 → 观察
        return ActionLevel.OBSERVE;
    }

    // ===============================
    // 2️⃣ Confidence = 可信度（卖点）
    // ===============================
    private double calculateConfidence(Incident incident) {
        double confidence = 0.5;

        if (!incident.getConfirmedRootCauses().isEmpty()) {
            confidence += 0.3;
        }
        if (!incident.getSeverityDecisions().isEmpty()) {
            confidence += 0.1;
        }
        if (incident.getTokenUsage() > 500) {
            confidence += 0.1;
        }

        return Math.min(confidence, 0.95);
    }

    // ===============================
    // 3️⃣ Why = 人话解释
    // ===============================
    private List<String> buildWhy(Incident incident) {
        List<String> why = new ArrayList<>();

        why.add("系统评估事故严重级别为 " + incident.getSeverityLevel());

        if (!incident.getConfirmedRootCauses().isEmpty()) {
            why.add("已确认根因：" + incident.getConfirmedRootCauses().get(0));
        } else if (!incident.getSuspectedRootCauses().isEmpty()) {
            why.add("存在推测根因：" + incident.getSuspectedRootCauses().get(0));
        } else {
            why.add("当前未发现明确根因");
        }

        return why;
    }

    // ===============================
    // 4️⃣ Top Signals = 决策证据
    // ===============================
//    private List<String> extractTopSignals(Incident incident) {
//        return incident.getSeverityDecisions()
//                .stream()
//                .sorted((a, b) -> Integer.compare(b.getWeight(), a.getWeight()))
//                .limit(3)
//                .map(d -> d.getSource() + ": " + d.getReason())
//                .toList();
//    }
    private List<String> extractTopSignals(Incident incident) {

        List<String> signals = new ArrayList<>();

        if (incident.getRawLog() != null) {

            long errorCount =
                    incident.getRawLog()
                            .lines()
                            .filter(l -> l.contains("ERROR"))
                            .count();

            signals.add(
                    "日志中检测到 ERROR " + errorCount + " 次"
            );

            String firstError =
                    incident.getRawLog()
                            .lines()
                            .filter(l -> l.contains("ERROR"))
                            .findFirst()
                            .orElse(null);

            if (firstError != null) {
                signals.add(
                        "[EVIDENCE] " +
                                firstError.substring(
                                        0,
                                        Math.min(200, firstError.length())
                                )
                );
            }
        }

        if (signals.isEmpty()) {

            signals.add(
                    "[UNKNOWN][LOW] No strong signals detected"
            );
        }

        return signals;
    }

    // ===============================
    // 5️⃣ Next Actions = 行动建议
    // ===============================
    private List<String> buildNextActions(Incident incident) {
        List<String> actions = new ArrayList<>();

        if (incident.isUserImpact()) {
            actions.add("立即通知值班负责人");
            actions.add("评估是否需要回滚最近变更");
        } else {
            actions.add("持续观察系统指标");
        }

        return actions;
    }

    // ===============================
    // 6️⃣ Capabilities = 付费钩子
    // ===============================
    private InsightCapabilities resolveCapabilities() {
        InsightCapabilities cap = new InsightCapabilities();
        cap.setAdvancedRca(false);
        cap.setTrendAnalysis(false);
        cap.setSlaImpact(false);
        return cap;
    }
}
