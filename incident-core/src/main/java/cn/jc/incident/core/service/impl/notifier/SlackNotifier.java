package cn.jc.incident.core.service.impl.notifier;

import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.service.notifier.MultiChannelNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackNotifier implements MultiChannelNotifier {

    @Value("${incident.notify.slack.webhook:}")
    private String webhook;

    private final RestTemplate restTemplate;

    @Override
    public void notify(IncidentInsight insight) {

        if (webhook == null || webhook.isBlank()) return;
        if (insight == null) return;

        String message = NotificationMessageBuilder.buildPlainText(insight);

        try {
            restTemplate.postForObject(
                    webhook,
                    Map.of("text", message),
                    String.class
            );
        } catch (Exception e) {
            log.error("Slack notify failed", e);
        }
    }

    @Override
    public void notify(String msg) {

    }
}
