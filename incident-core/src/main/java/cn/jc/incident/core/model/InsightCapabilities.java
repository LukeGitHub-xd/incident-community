package cn.jc.incident.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsightCapabilities {
    private boolean advancedRca;
    private boolean trendAnalysis;
    private boolean slaImpact;
}
