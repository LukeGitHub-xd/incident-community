package cn.jc.incident.core.dto.view;

import cn.jc.incident.core.dto.response.ExecutiveSummary;
import cn.jc.incident.core.model.ServiceImpact;
import cn.jc.incident.core.model.TimelineEvent;
import cn.jc.incident.core.model.TrendPoint;
import cn.jc.incident.core.plan.ActionPlan;
import lombok.Data;

import java.util.List;
@Data
public class IncidentReportView {

    private ExecutiveSummary executiveSummary;

    private ImpactOverview impact;

    private RootCauseOverview rootCause;

    private List<KeySignalView> keySignals;

    private ActionPlan actionPlan;

    private ConfidenceView confidence;

    /**
     * 新增：事故时间线
     */
    private List<TimelineEvent> timeline;

    /**
     * 新增：异常趋势
     */
    private List<TrendPoint> errorTrend;

    /**
     * 新增：服务影响热力
     */
    private List<ServiceImpact> serviceImpact;
}