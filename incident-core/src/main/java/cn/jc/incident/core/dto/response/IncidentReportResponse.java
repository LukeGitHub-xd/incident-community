package cn.jc.incident.core.dto.response;

import cn.jc.incident.core.dto.view.IncidentReportView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentReportResponse {

    /** Incident ID（后续查询用） */
    private String incidentId;

    /** 结构化报告（你已有） */
    private IncidentReportView report;

    /** Markdown（给人看 / 导出） */
    private String markdown;
}
