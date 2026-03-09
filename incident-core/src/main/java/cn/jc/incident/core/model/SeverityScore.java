package cn.jc.incident.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeverityScore {

    /** 该 Scorer 给出的严重级别 */
    private SeverityLevel finalLevel;

    /** 该 Scorer 产生的结构化决策记录 */
    private List<SeverityDecision> decisions = new ArrayList<>();

    /** 是否明确影响用户 */
    private boolean userImpact;


    /** 兼容旧用法：给人看的理由（可选） */
    public List<String> getReasons() {
        if (decisions == null) {
            return List.of();
        }
        return decisions.stream()
                .map(SeverityDecision::getReason)
                .toList();
    }
}
