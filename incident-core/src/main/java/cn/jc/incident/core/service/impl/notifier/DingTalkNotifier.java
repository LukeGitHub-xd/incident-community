package cn.jc.incident.core.service.impl.notifier;

import cn.jc.incident.core.config.NotifyProperties;
import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.service.notifier.MultiChannelNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DingTalkNotifier implements MultiChannelNotifier {

    private final RestTemplate restTemplate;
    private final NotifyProperties properties;

    @Override
    public void notify(IncidentInsight insight) {

        var config = properties.getDingtalk();

        if (!config.isEnabled()) return;
        if (config.getWebhook() == null || config.getWebhook().isBlank()) return;

        String message = NotificationMessageBuilder.buildPlainText(insight);

        String body = """
        {
          "msgtype": "text",
          "text": {
            "content": "%s"
          }
        }
        """.formatted(message.replace("\"", "\\\""));

        try {
            restTemplate.postForEntity(config.getWebhook(), body, String.class);
        } catch (Exception e) {
            log.error("DingTalk notify failed", e);
        }
    }

    @Override
    public void notify(String msg) {

    }
}