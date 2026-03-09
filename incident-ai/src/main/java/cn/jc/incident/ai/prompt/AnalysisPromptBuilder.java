package cn.jc.incident.ai.prompt;

import cn.jc.incident.ai.prompt.model.IncidentContext;

import java.util.List;
import java.util.StringJoiner;

public class AnalysisPromptBuilder implements PromptBuilder {

    private final IncidentContext context;

    public AnalysisPromptBuilder(IncidentContext context) {
        this.context = context;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();

        appendSection(sb, "事故背景", context.getIncidentSummary());
        appendSection(sb, "时间范围", context.getTimeRange());
        appendListSection(sb, "涉及系统 / 服务", context.getInvolvedServices());
        appendListSection(sb, "已确认事实", context.getKnownFacts());
        appendListSection(sb, "监控与指标信息", context.getMetrics());
        appendListSection(sb, "相关日志", context.getLogs());

        sb.append("\n");
        sb.append("请基于以上【已知上下文与日志信息】，对本次线上事故进行分析。\n");
        sb.append("分析时请严格遵守 System Prompt 中定义的角色和规则。\n");

        return sb.toString();
    }

    private void appendSection(StringBuilder sb, String title, String content) {
        if (content == null || content.isBlank()) {
            return;
        }
        sb.append("【").append(title).append("】\n");
        sb.append(content).append("\n\n");
    }

    private void appendListSection(StringBuilder sb, String title, List<String> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        sb.append("【").append(title).append("】\n");

        StringJoiner joiner = new StringJoiner("\n");
        for (String item : items) {
            joiner.add("- " + item);
        }

        sb.append(joiner).append("\n\n");
    }
}
