package cn.jc.incident.core.service.impl.notifier;

import cn.jc.incident.core.model.IncidentInsight;

import java.util.List;

public class NotificationMessageBuilder {

    public static String buildPlainText(IncidentInsight insight) {

        return """
🚨 事故分析报告

严重等级: %s
置信度: %.0f%%

已确认根因:
%s

建议:
%s
""".formatted(
                insight.getSeverity(),
                insight.getConfidence() * 100,
                formatList(insight.getConfirmedRootCauses()),
                formatList(insight.getNextActions())
        );
    }

    private static String formatList(List<String> list) {
        if (list == null || list.isEmpty()) return "暂无";

        return list.stream()
                .limit(3)
                .map(s -> "- " + s)
                .reduce((a, b) -> a + "\n" + b)
                .orElse("暂无");
    }
}
