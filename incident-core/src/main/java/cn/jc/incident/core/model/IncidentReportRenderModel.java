package cn.jc.incident.core.model;

import cn.jc.incident.core.dto.view.IncidentReportView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentReportRenderModel {

    /** 报告主体（你已经有完整结构） */
    private IncidentReportView report;

    /** 用于页面 / PDF 展示的元信息 */
    private String incidentId;
    private LocalDateTime generatedAt;
}
