package cn.jc.incident.core.plan;

import lombok.Data;

import java.util.List;

@Data
public class ActionPlan {

    /** 立刻要做的事（今天） */
    private List<String> immediateActions;

    /** 短期改进（本周） */
    private List<String> shortTermImprovements;

    /** 长期优化（技术债） */
    private List<String> longTermImprovements;

    /** 是否建议升级为事故复盘 */
    private boolean recommendPostmortem;
}
