package cn.jc.incident.core.rule.model;

import lombok.Data;

import java.util.List;

@Data
public class ConditionGroup {

    private List<Condition> all;
    private List<Condition> any;
}
