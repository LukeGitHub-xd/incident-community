package cn.jc.incident.core.evaluation.impl;

import cn.jc.incident.core.context.IncidentAnalysisContext;
import cn.jc.incident.core.evaluation.SeverityEvaluator;
import cn.jc.incident.core.model.Incident;
import cn.jc.incident.core.model.SeverityLevel;
import cn.jc.incident.core.model.SeverityScore;
import org.springframework.stereotype.Component;

/**
 * 开源简化版：统一返回 P3 级别
 */
@Component
public class CompositeSeverityEvaluator implements SeverityEvaluator {

    @Override
    public SeverityScore evaluate(IncidentAnalysisContext context) {
        Incident incident = context.getIncident();
        
        // 开源版固定为 P3 级别
        SeverityLevel finalLevel = SeverityLevel.P3;
        boolean userImpact = false;
        
        incident.setSeverityLevel(finalLevel);
        incident.setUserImpact(userImpact);
        
        return new SeverityScore(finalLevel, null, userImpact);
    }

}
