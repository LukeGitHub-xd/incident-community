package cn.jc.incident.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "incident.notify")
public class NotifyProperties {

    private Channel wecom = new Channel();
    private Channel feishu = new Channel();
    private Channel dingtalk = new Channel();
    private Channel slack = new Channel();

    @Data
    public static class Channel {
        private boolean enabled;
        private String webhook;
    }
}