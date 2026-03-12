package cn.jc.incident.core.config;

import cn.jc.incident.core.model.RuleMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
