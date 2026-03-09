package cn.jc.incident.core.evaluation;

import cn.jc.incident.core.context.IncidentAnalysisContext;
import cn.jc.incident.core.model.SeverityScore;


public interface SeverityEvaluator {

    SeverityScore evaluate(IncidentAnalysisContext context);
}
