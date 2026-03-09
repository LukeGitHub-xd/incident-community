package cn.jc.incident.core.context;

import cn.jc.incident.core.model.AIAnalysisResult;
import cn.jc.incident.core.model.Incident;
import cn.jc.incident.core.model.SeverityScore;
import cn.jc.incident.core.model.TokenBudget;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 一次 Incident 分析的完整会话
 * 生命周期：仅存在于 Service 调用过程中
 */
@Getter
@AllArgsConstructor
public class IncidentAnalysisContext {

    // === 会话元信息 ===
    private final String sessionId;
    private final LocalDateTime startTime;

    // === 核心事实（唯一事实源）===
    private final Incident incident;

    // === Budget & Token ===
    private final TokenBudget budget;
    private int consumedTokens = 0;

    // === 分析中间产物 ===
    private String cleanedLog;
    private AIAnalysisResult aiResult;
    private SeverityScore severityScore;

    // === 决策日志（Explainability）===
    private final List<String> decisionLogs = new ArrayList<>();

    // ===== 构造 =====
    public IncidentAnalysisContext(Incident incident, TokenBudget budget) {
        this.sessionId = UUID.randomUUID().toString();
        this.startTime = LocalDateTime.now();
        this.incident = incident;
        this.budget = budget;
    }

    // ===== 行为方法（禁止随意 set）=====

    public void recordCleanedLog(String log) {
        this.cleanedLog = log;
        decisionLogs.add(
                "Log preprocessed, length=" + (log == null ? 0 : log.length())
        );
    }

    public boolean canCallAI() {
        return budget != null && budget.availableForLog() > 0;
    }

    public void consumeTokens(int tokens) {
        this.consumedTokens += tokens;
        if (budget != null) {
            budget.consume(tokens);
        }
        decisionLogs.add("Consumed " + tokens + " tokens");
    }

    public void recordAIResult(AIAnalysisResult result) {
        this.aiResult = result;
        decisionLogs.add("AI analysis completed");
    }

    public void recordSeverityScore(SeverityScore score) {
        this.severityScore = score;
        decisionLogs.add("Severity evaluated as " + score.getFinalLevel());
    }
}
