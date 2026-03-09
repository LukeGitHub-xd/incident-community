package cn.jc.incident.core.rule.model;

import lombok.Data;

@Data
public class RuleConfig {

    private String name;
    private boolean enabled = true;
    private int priority = 0;
    private String severity;

    private MatchScope match;

    private ConditionGroup when;
}

