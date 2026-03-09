package cn.jc.incident.core.dto.view;

import lombok.Data;

import java.util.List;

@Data
public class ConfidenceView {

    /** AI 信心值 0~1 */
    private double confidenceScore;

    /** 信心等级（低 / 中 / 高） */
    private String confidenceLevel;

    /** 信心来源说明 */
    private List<String> confidenceFactors;

    /** 注意事项 / 风险提示 */
    private List<String> caveats;
}
