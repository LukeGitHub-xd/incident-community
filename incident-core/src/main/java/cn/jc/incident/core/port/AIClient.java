package cn.jc.incident.core.port;

import cn.jc.incident.core.model.AIAnalysisResult;
import cn.jc.incident.core.context.IncidentAnalysisContext;

public interface AIClient {


    /**
     * 基于分析会话进行 AI 分析
     */
    AIAnalysisResult analyze(IncidentAnalysisContext context);
}