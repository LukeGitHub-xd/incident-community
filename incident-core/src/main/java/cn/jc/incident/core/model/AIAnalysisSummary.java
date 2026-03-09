package cn.jc.incident.core.model;

import lombok.Data;

import java.util.List;

@Data
public class AIAnalysisSummary {

    /** 已确认根因 */
    private List<String> confirmedRootCauses;

    /** 推测中的根因 */
    private List<String> suspectedRootCauses;

    /** 当前不确定点 */
    private List<String> uncertainties;

    /** AI 给出的处置建议 */
    private List<String> recommendations;
}
