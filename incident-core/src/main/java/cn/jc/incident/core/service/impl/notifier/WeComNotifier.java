package cn.jc.incident.core.service.impl.notifier;

import cn.jc.incident.core.config.NotifyProperties;
import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.model.IncidentReportRenderModel;
import cn.jc.incident.core.report.renderer.WechatMarkdownRenderer;
import cn.jc.incident.core.service.IncidentService;
import cn.jc.incident.core.service.notifier.MultiChannelNotifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeComNotifier implements MultiChannelNotifier {

    private final RestTemplate restTemplate;
    private final NotifyProperties properties;
    private final ObjectMapper objectMapper;
    private final WechatMarkdownRenderer wechatMarkdownRenderer;
    private final IncidentService incidentService;

    @Override
    public void notify(IncidentInsight insight) {

        var config = properties.getWecom();

        if (!config.isEnabled()) return;
        if (config.getWebhook() == null || config.getWebhook().isBlank()) return;

        try {

            IncidentReportRenderModel renderModel =
                    incidentService.generateReport(insight.getIncidentId());

            log.info("timeline={}", renderModel.getReport().getTimeline());
            log.info("trend={}", renderModel.getReport().getErrorTrend());
            log.info("impact={}", renderModel.getReport().getServiceImpact());
            log.info("shortTerm={}", renderModel.getReport().getActionPlan().getShortTermImprovements());
            // ✅ 渲染完整 Markdown 报告
            String markdownContent = wechatMarkdownRenderer.render(renderModel);

            // ===== 构建企业微信 Markdown payload =====
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("msgtype", "markdown");

            ObjectNode markdownNode = objectMapper.createObjectNode();
            markdownNode.put("content", markdownContent);

            payload.set("markdown", markdownNode);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(
                    new MediaType("application", "json", StandardCharsets.UTF_8)
            );

            HttpEntity<String> request = new HttpEntity<>(
                    objectMapper.writeValueAsString(payload),
                    headers
            );

            restTemplate.postForEntity(config.getWebhook(), request, String.class);

        } catch (Exception e) {
            log.error("WeCom notify failed", e);
        }
    }


    /**
     * 发送文本通知
     */
    @Override
    public void notify(String message) {

        var config = properties.getWecom();

        if (!config.isEnabled()) return;
        if (config.getWebhook() == null || config.getWebhook().isBlank()) return;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of(
                    "msgtype", "text",
                    "text", Map.of("content", message)
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    config.getWebhook(),
                    request,
                    String.class
            );

            log.info("企业微信通知发送成功: {}, 返回: {}", message, response.getBody());

        } catch (Exception e) {
            log.error("企业微信通知发送失败: {}", message, e);
        }
    }
}
