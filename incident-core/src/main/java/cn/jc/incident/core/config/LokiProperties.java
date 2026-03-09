package cn.jc.incident.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "incident.loki")
public class LokiProperties {

    /**
     * 例如：http://localhost:3100
     */
    private String baseUrl;


    /**
     * 支持可选 label
     */
    private String serviceLabel = "service_name";
    private String envLabel;          // 可为空
    private String namespaceLabel;    // 可为空
    private String containerLabel = "container";
}