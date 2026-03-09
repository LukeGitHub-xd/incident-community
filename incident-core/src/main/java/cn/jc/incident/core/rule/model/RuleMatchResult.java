package cn.jc.incident.core.rule.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class RuleMatchResult {

    private String ruleName;
    private String severity;
    private int priority;

//    private Map<String, Object> matchedFields;
}