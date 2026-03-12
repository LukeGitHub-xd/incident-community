package cn.jc.incident.core.service;

import cn.jc.incident.core.dto.request.CreateIncidentRequest;
import cn.jc.incident.core.dto.response.IncidentInsightResponse;
import cn.jc.incident.core.dto.view.IncidentReportView;
import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.model.IncidentReportRenderModel;
import cn.jc.incident.core.model.ReportPlan;
import cn.jc.incident.core.model.TokenBudget;

public interface IncidentService {
    /**
     * 分析日志（开源简化版）
     */
    IncidentInsight analyze(CreateIncidentRequest request, TokenBudget budget);

    /**
     * 获取事故洞察
     */
    IncidentInsightResponse getInsight(String incidentId);

    /**
     * 获取事件报告（开源版统一为标准报告）
     */
    IncidentReportView getIncidentReport(String incidentId, ReportPlan plan);

    /**
     * 生成报告模型
     */
    IncidentReportRenderModel generateReport(String incidentId);
}
