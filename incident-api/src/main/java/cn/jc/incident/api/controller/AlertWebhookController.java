package cn.jc.incident.api.controller;

import cn.jc.incident.core.model.AlertManagerPayload;
import cn.jc.incident.core.rule.engine.DslRuleEngine;
import cn.jc.incident.core.rule.model.RuleMatchResult;
import cn.jc.incident.core.service.AlertOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alert")
@RequiredArgsConstructor
public class AlertWebhookController {

    private final AlertOrchestratorService orchestrator;
    private final DslRuleEngine ruleEngine;

    @PostMapping("/webhook")
    public ResponseEntity<String> receive(
            @RequestBody AlertManagerPayload payload) {

        Map<String, Object> context = buildContext(payload);

        List<RuleMatchResult> matches =
                ruleEngine.evaluate(context);

        if (!matches.isEmpty()) {

            // 命中规则才进入 AI 分析流程
            orchestrator.handleAsync(payload);
        }

        return ResponseEntity.ok("received");
    }

    private Map<String, Object> buildContext(
            AlertManagerPayload payload) {

        Map<String, Object> context = new HashMap<>();

        if (payload.getCommonLabels() != null) {

            context.put("service",
                    payload.getCommonLabels().get("service"));

            context.put("env",
                    payload.getCommonLabels().get("env"));

            context.put("alertname",
                    payload.getCommonLabels().get("alertname"));
        }

        return context;
    }
}
