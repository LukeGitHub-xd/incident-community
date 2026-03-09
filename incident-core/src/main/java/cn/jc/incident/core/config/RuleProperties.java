package cn.jc.incident.core.config;

import cn.jc.incident.core.model.RuleMode;
import cn.jc.incident.core.rule.model.RuleConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "incident.rule")
public class RuleProperties {

    private RuleMode mode = RuleMode.AUTO;

    /**
     * 内置规则扫描间隔（秒）
     */
    private int scanIntervalSeconds = 30;

}
