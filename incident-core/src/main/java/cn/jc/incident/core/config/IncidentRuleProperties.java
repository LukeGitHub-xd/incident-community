package cn.jc.incident.core.config;

import cn.jc.incident.core.rule.model.RuleConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "incident")
public class IncidentRuleProperties {

    /**
     * 平台rule规则
     */
    private List<RuleConfig> rules;
    private List<String> services;
}
