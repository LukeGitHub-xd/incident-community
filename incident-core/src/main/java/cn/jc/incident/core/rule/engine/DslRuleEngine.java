package cn.jc.incident.core.rule.engine;

import cn.jc.incident.core.config.IncidentRuleProperties;
import cn.jc.incident.core.rule.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DslRuleEngine {

    private final IncidentRuleProperties incidentRuleProperties;

    public List<RuleMatchResult> evaluate(Map<String, Object> context) {

        if (incidentRuleProperties.getRules() == null) {
            return Collections.emptyList();
        }
        log.info("Rules: {}", incidentRuleProperties.getRules());

        String service = (String) context.get("service");
        log.info("Service: {}", service);
        return incidentRuleProperties.getRules().stream()
                .filter(RuleConfig::isEnabled)
                .sorted(Comparator.comparingInt(RuleConfig::getPriority).reversed())
                .filter(rule -> matchService(rule.getMatch(), service))
                .filter(rule -> matchConditions(rule.getWhen(), context))
                .map(rule -> new RuleMatchResult(
                        rule.getName(),
                        rule.getSeverity(),
                        rule.getPriority()

                ))
                .collect(Collectors.toList());
    }

    private boolean matchService(MatchScope scope, String service) {

        if (scope == null || service == null) return true;

        if (scope.getServices() != null) {
            for (String pattern : scope.getServices()) {
                if (wildcardMatch(pattern, service)) {
                    return true;
                }
            }
        }

        if (scope.getServicesRegex() != null) {
            return service.matches(scope.getServicesRegex());
        }

        return false;
    }

    private boolean wildcardMatch(String pattern, String service) {

        if ("*".equals(pattern)) return true;

        String regex = pattern.replace("*", ".*");
        log.info("Regex: {}", regex);
        return service.matches(regex);
    }

    private boolean matchConditions(ConditionGroup group,
                                    Map<String, Object> ctx) {

        if (group == null) return true;

        if (group.getAll() != null) {
            return group.getAll().stream()
                    .allMatch(c -> matchCondition(c, ctx));
        }

        if (group.getAny() != null) {
            return group.getAny().stream()
                    .anyMatch(c -> matchCondition(c, ctx));
        }

        return false;
    }

    private boolean matchCondition(Condition c,
                                   Map<String, Object> ctx) {

        Object actual = ctx.get(c.getField());
        if (actual == null) return false;

        switch (c.getOp()) {

            case "eq":
                return actual.toString()
                        .equals(c.getValue().toString());

            case "gt":
                return Double.parseDouble(actual.toString())
                        > Double.parseDouble(c.getValue().toString());

            case "lt":
                return Double.parseDouble(actual.toString())
                        < Double.parseDouble(c.getValue().toString());

            case "contains":
                return actual.toString()
                        .contains(c.getValue().toString());

            case "regex":
                return actual.toString()
                        .matches(c.getValue().toString());

            default:
                return false;
        }
    }
}
