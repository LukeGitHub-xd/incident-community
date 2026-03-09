package cn.jc.incident.core.dto.view;

import lombok.Data;

@Data
public class KeySignalView {

    /** 信号来源（LOG / METRIC / RULE / AI） */
    private String source;

    /** 信号描述（人话） */
    private String description;

    /** 重要性（HIGH / MEDIUM / LOW） */
    private String importance;

    /** 对结论的影响 */
    private String impactOnDecision;
}
