package cn.jc.incident.core.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AIAnalysisResult {

    /** 事故整体摘要 */
    private String summary;

    /** 已确认结论（有明确证据） */
    private List<ConfirmedFinding> confirmedFindings;

    /** 推测性结论 */
    private List<SuspectedFinding> suspectedFindings;

    /** 当前无法确认的点 */
    private List<String> uncertainties;

    /** 建议的后续行动 */
    private List<String> recommendedNextSteps;

    /**
     * 建议严重程度
     * P0 / P1 / P2 / P3
     */
    private String suggestedSeverity;
}

