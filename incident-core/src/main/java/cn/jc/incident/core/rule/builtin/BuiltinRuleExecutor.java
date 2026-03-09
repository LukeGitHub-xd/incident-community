package cn.jc.incident.core.rule.builtin;

import cn.jc.incident.core.config.RuleProperties;
import cn.jc.incident.core.model.AlertManagerPayload;
import cn.jc.incident.core.model.RuleMode;
import cn.jc.incident.core.rule.engine.DslRuleEngine;
import cn.jc.incident.core.service.AlertOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: incident
 * @package: cn.jc.incident.core.rule.builtin
 * @className: BuiltinRuleExecutor
 * @author: dongdong.xue
 * @description: BuiltinRuleExecutor
 * @date: 2026/2/16 17:51
 * @version: 1.0
 */

@Component
@RequiredArgsConstructor
public class BuiltinRuleExecutor {

    private final RuleProperties ruleProperties;
    private final DslRuleEngine ruleEngine;
    private final AlertOrchestratorService orchestrator;

    public void execute(Map<String, Object> inputContext) {

        if (ruleProperties.getMode() == RuleMode.EXTERNAL) {
            return;
        }

        Map<String, Object> context = new HashMap<>(inputContext);

        var results = ruleEngine.evaluate(context);

        if (!results.isEmpty()) {
            // 取最高优先级
            var top = results.get(0);
            context.put("severity", top.getSeverity());
        } else {
            context.put("severity", "P3"); // 默认
        }

        orchestrator.handleAsync(buildPayload(context));
    }

    private AlertManagerPayload buildPayload(
            Map<String, Object> context) {

        var payload = new AlertManagerPayload();
        payload.setCommonLabels(new HashMap<>());

        context.forEach((k, v) ->
                payload.getCommonLabels().put(k, String.valueOf(v)));

        return payload;
    }
}