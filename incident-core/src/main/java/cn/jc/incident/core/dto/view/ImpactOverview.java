package cn.jc.incident.core.dto.view;

import cn.jc.incident.core.model.ImpactScope;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImpactOverview {

    /** 受影响服务 */
    private String serviceName;

    /** 环境（prod / staging） */
    private String env;

    /** 发生时间 */
    private LocalDateTime occurTime;

    /** 用户影响说明（人话） */
    private String userImpactDescription;

    /** 影响范围估计 */
    private ImpactScope scope;

    /** 是否影响 SLA（付费能力） */
    private Boolean slaAtRisk;
}
