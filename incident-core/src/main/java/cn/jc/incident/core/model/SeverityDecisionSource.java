package cn.jc.incident.core.model;

public enum SeverityDecisionSource {

    RULE,       // 明确规则（强确定性）
    AI,         // AI 推断
    METRIC,     // 指标 / SLA / 监控
    MANUAL      // 人工介入（未来）
}
