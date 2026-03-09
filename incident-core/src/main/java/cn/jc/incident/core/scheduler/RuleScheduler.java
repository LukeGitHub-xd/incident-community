package cn.jc.incident.core.scheduler;

import cn.jc.incident.core.config.IncidentRuleProperties;
import cn.jc.incident.core.rule.builtin.BuiltinRuleExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RuleScheduler {

    private final BuiltinRuleExecutor executor;
    private final IncidentRuleProperties ruleProperties;

    @Scheduled(fixedDelayString =
            "${incident.rule.scan-interval-seconds:30}000")
    public void scan() {

        List<String> services = ruleProperties.getServices();

        if (services == null || services.isEmpty()) {
            log.warn("No services configured");
            return;
        }

        for (String service : services) {

            Map<String, Object> ctx = new HashMap<>();
            ctx.put("service", service);

            executor.execute(ctx);
        }
    }
    private Set<String> collectServices() {

        Set<String> services = new HashSet<>();

        for (var rule : ruleProperties.getRules()) {

            if (!rule.isEnabled()) continue;

            if (rule.getMatch() == null) continue;

            if (rule.getMatch().getServices() != null) {
                services.addAll(rule.getMatch().getServices());
            }
        }

        return services;
    }
    /**
     * 从 Loki 返回结果中解析 ErrorRate
     */
    private double extractErrorRate(String logs) {

        if (logs == null || logs.isBlank()) {
            return 0;
        }

        for (String line : logs.split("\n")) {

            if (line.startsWith("ErrorRate:")) {

                try {
                    String value = line
                            .replace("ErrorRate:", "")
                            .replace("%", "")
                            .trim();

                    return Double.parseDouble(value);
                } catch (Exception e) {
                    log.warn("Failed to parse errorRate from logs: {}", line);
                    return 0;
                }
            }
        }

        return 0;
    }
}