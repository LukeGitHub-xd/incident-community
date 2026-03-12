package cn.jc.incident.core.service.impl;

import cn.jc.incident.core.analysis.RootCauseAnalyzer;
import cn.jc.incident.core.config.LokiClient;
import cn.jc.incident.core.dto.request.CreateIncidentRequest;
import cn.jc.incident.core.model.AlertManagerPayload;
import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.model.LokiQueryResult;
import cn.jc.incident.core.model.TokenBudget;
import cn.jc.incident.core.service.AlertOrchestratorService;
import cn.jc.incident.core.service.IncidentService;
import cn.jc.incident.core.service.impl.notifier.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertOrchestratorServiceImpl implements AlertOrchestratorService {

    private final LokiClient lokiClient;
    private final IncidentService incidentService;
    private final NotificationDispatcher dispatcher;
    private final RootCauseAnalyzer rootCauseAnalyzer;

    @Async
    @Override
    public void handleAsync(AlertManagerPayload payload) {
        try {
            handle(payload);
        } catch (Exception e) {
            log.error("Alert async error", e);
        }
    }

    @Override
    public void handle(AlertManagerPayload payload) {

        Map<String, String> labels = payload.getCommonLabels();

        String service = labels.getOrDefault("service", "");
        String env = labels.getOrDefault("env", "");

        log.info("AlertManager labels: {}", labels);

        if (service.isBlank()) {
            return;
        }

        LokiQueryResult result =
                lokiClient.queryRecentErrorLogs(service, env, 5);

        String logs = result.getLogs();
        int errorCount = result.getErrorCount();

        if (errorCount == 0) {
            log.info("No ERROR logs in last 5 minutes for {}", service);
            return;
        }

        String firstPosition = result.getFirstErrorPosition();

        String rootCause =
                rootCauseAnalyzer.extractRootCause(logs);

        String location =
                rootCauseAnalyzer.extractErrorLocation(logs);

        String evidence =
                rootCauseAnalyzer.extractEvidence(logs);

        String exceptionType =
                rootCauseAnalyzer.classifyException(logs);

        log.info("ERROR logs detected for {}", service);
        log.info("RootCause: {}", rootCause);
        log.info("Location: {}", location);
        log.info("ExceptionType: {}", exceptionType);
        log.info("ErrorCount: {}", errorCount);

        CreateIncidentRequest request =
                new CreateIncidentRequest();

        request.setServiceName(service);
        request.setEnv(env);
        request.setLogContent(logs);

        String summary =
                "Incident Signals\n" +
                        "service=" + service + "\n" +
                        "env=" + env + "\n" +
                        "errorCount=" + errorCount + "\n" +
                        "firstError=" + firstPosition + "\n" +
                        "rootCause=" + rootCause + "\n" +
                        "location=" + location + "\n" +
                        "exceptionType=" + exceptionType + "\n\n" +
                        "Evidence:\n" +
                        evidence;

        request.setChangeSummary(summary);

        IncidentInsight insight =
                incidentService.analyze(
                        request,
                        TokenBudget.pro()
                );

        dispatcher.dispatch(insight);
    }
}