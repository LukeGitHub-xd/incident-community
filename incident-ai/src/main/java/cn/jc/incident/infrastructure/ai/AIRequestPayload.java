package cn.jc.incident.infrastructure.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIRequestPayload {

    private String serviceName;
    private String env;
    private String changeSummary;
    private String cleanedLog;

    /** 最大可用 token，来自 budget */
    private int maxTokens;
}
