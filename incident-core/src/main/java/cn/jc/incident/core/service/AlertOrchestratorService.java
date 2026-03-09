package cn.jc.incident.core.service;

import cn.jc.incident.core.model.AlertManagerPayload;

public interface AlertOrchestratorService {
    void handle(AlertManagerPayload payload);

    void handleAsync(AlertManagerPayload payload);
}
