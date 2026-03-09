package cn.jc.incident.core.model;

import lombok.Getter;

/**
 * 事故当前状态（老板/管理视角）
 */
@Getter
public enum IncidentStatus {
    ONGOING("进行中"),
    RESOLVED("已恢复"),
    MONITORED("已监控"),
    MITIGATING("处理中"),
    ;

    private final String zh;

    IncidentStatus(String zh) {
        this.zh = zh;
    }

}

