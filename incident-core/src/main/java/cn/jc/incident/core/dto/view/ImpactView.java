package cn.jc.incident.core.dto.view;

import lombok.Data;

@Data
public class ImpactView {

    private boolean userImpact;     // 是否影响用户
    private String description;     // 影响描述（无/部分/大面积）
}
