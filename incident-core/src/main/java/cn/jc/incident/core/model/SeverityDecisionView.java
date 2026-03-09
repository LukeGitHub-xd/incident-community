package cn.jc.incident.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeverityDecisionView {

    /** 来源 */
    private String source;      // RULE / AI / METRIC

    /** 决策器 */
    private String scorer;

    /** 给出的等级 */
    private SeverityLevel level;

    /** 人类可读原因 */
    private String reason;

    /** 决策强度（HIGH / MEDIUM / LOW） */
    private String confidence;
}
