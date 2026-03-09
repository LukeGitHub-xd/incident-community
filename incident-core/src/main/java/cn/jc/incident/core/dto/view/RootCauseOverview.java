package cn.jc.incident.core.dto.view;

import cn.jc.incident.core.model.RootCauseCategory;
import lombok.Data;

import java.util.List;

@Data
public class RootCauseOverview {

    /** 已确认根因（有证据） */
    private List<String> confirmedRootCauses;

    /** 高概率推测根因 */
    private List<String> suspectedRootCauses;

    /** 当前不确定点 */
    private List<String> uncertainties;

    /** 根因类型（用于统计 & 复盘） */
    private RootCauseCategory category;
}
