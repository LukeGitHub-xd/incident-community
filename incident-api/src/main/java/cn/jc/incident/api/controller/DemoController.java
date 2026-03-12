package cn.jc.incident.api.controller;

import cn.jc.incident.core.dto.request.CreateIncidentRequest;
import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.model.TokenBudget;
import cn.jc.incident.core.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {

    private final IncidentService incidentService;

    @PostMapping("/analyze")
    public Map<String,Object> analyze(@RequestBody Map<String,String> req){

        String log = req.get("log");

        CreateIncidentRequest request = new CreateIncidentRequest();
        request.setServiceName("demo-service");
        request.setEnv("demo");
        request.setLogContent(log);
        request.setChangeSummary("demo analyze");

        IncidentInsight insight =
                incidentService.analyze(request, TokenBudget.free());

        Map<String,Object> result = new HashMap<>();

        result.put("severity", insight.getSeverity());
        result.put("action", insight.getActionStr());
        result.put("confidence", insight.getConfidence());
        result.put("rootCause", insight.getConfirmedRootCauses());
        result.put("suspected", insight.getSuspectedRootCauses());
        result.put("timeline", insight.getTimeline());

        return result;
    }
}