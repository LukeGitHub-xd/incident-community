package cn.jc.incident.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIncidentRequest {
    private String serviceName;
    private String env;
    private String occurTime;
    private String changeSummary;
    private String logContent;
    private String rawLog;
    private String alertName;
//    private String title;
    private String severity;   // 🔥 用 severity 驱动预算

}