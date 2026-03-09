package cn.jc.incident.core.model;

import lombok.Getter;

@Getter
public enum ActionLevel {
    IMMEDIATE("立即处理"),      // 立刻处理 / 拉人
    INVESTIGATE("需要跟进"),    // 需要跟进
    OBSERVE("先观察"),         // 先观察
    ;
    private final String zh;

    ActionLevel(String zh) {
        this.zh = zh;
    }
}
