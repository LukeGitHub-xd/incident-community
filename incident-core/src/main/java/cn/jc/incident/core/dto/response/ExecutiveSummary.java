package cn.jc.incident.core.dto.response;

import cn.jc.incident.core.model.ActionLevel;
import cn.jc.incident.core.model.IncidentStatus;
import cn.jc.incident.core.model.SeverityLevel;
import lombok.Data;

@Data
public class ExecutiveSummary {
    /** 一句话结论（可直接贴到群里） */
    private String headline;

    /** 严重级别（P0/P1/P2） */
    private SeverityLevel severity;

    /** 是否确认用户影响 */
    private boolean userImpact;

    /** 建议动作（立即处理 / 调查 / 观察） */
    private ActionLevel recommendedAction;
    private String recommendedActionStr;

    /** 当前状态（已恢复 / 处理中 / 风险未解除） */
    private IncidentStatus status;
    private String statusStr;
}
