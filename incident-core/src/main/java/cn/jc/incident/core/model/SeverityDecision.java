package cn.jc.incident.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeverityDecision {

    /** 决策来源类型 */
    private SeverityDecisionSource source;

    /** 决策器 / scorer 名称 */
    private String scorer;

    /** 给出的 Severity */
    private SeverityLevel level;

    /** 决策理由（给人看） */
    private String reason;

    /** 决策权重（0~1，或规则内定义） */
    private int weight;
}
