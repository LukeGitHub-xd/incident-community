package cn.jc.incident.infrastructure.persistence.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 严重性决策 json
 *
 * @author xuedongdong
 * @date 2026/02/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeverityDecisionJson {

    /** 决策来源类型 */
    private String source;

    /** 决策器 / scorer 名称 */
    private String scorer;

    /** 给出的 Severity */
    private String level;

    /** 决策理由（给人看） */
    private String reason;

    /** 决策权重（0~1，或规则内定义） */
    private int weight;
}