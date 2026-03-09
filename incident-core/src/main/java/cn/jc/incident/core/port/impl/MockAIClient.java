package cn.jc.incident.core.port.impl;

import cn.jc.incident.core.context.IncidentAnalysisContext;
import cn.jc.incident.core.model.AIAnalysisResult;
import cn.jc.incident.core.model.ConfirmedFinding;
import cn.jc.incident.core.model.SuspectedFinding;
import cn.jc.incident.core.port.AIClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 开源版默认 AI 实现
 * 基于本地规则的轻量级分析
 */
@Component
public class MockAIClient implements AIClient {

    @Override
    public AIAnalysisResult analyze(IncidentAnalysisContext context) {
        String log = context.getCleanedLog();

        List<ConfirmedFinding> confirmed = new ArrayList<>();
        List<SuspectedFinding> suspected = new ArrayList<>();
        List<String> uncertainties = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        String severity = "P3";

        if (log != null && !log.isBlank()) {
            // 基础规则引擎
            if (log.contains("OutOfMemoryError") || log.contains("StackOverflowError")) {
                confirmed.add(new ConfirmedFinding("JVM 内存问题", extractEvidence(log, "Error")));
                severity = "P1";
                recommendations.add("检查堆内存配置");
                recommendations.add("分析内存泄漏");
            } else if (log.contains("ConnectionTimeout") || log.contains("ConnectException")) {
                confirmed.add(new ConfirmedFinding("网络连接异常", extractEvidence(log, "Connection")));
                severity = "P2";
                recommendations.add("检查网络配置");
                recommendations.add("验证服务可用性");
            } else if (log.contains("ERROR") || log.contains("Exception")) {
                confirmed.add(new ConfirmedFinding("应用程序错误", extractEvidence(log, "ERROR")));
                severity = "P2";
                recommendations.add("查看完整堆栈信息");
            } else if (log.contains("WARN")) {
                suspected.add(new SuspectedFinding("潜在性能问题", "日志中包含警告信息"));
                severity = "P3";
                recommendations.add("持续监控系统指标");
            } else {
                uncertainties.add("未发现明显异常模式");
                severity = "P3";
            }
        }

        return new AIAnalysisResult(
                "开源版自动分析摘要",
                confirmed,
                suspected,
                uncertainties,
                recommendations,
                severity
        );
    }

    private List<String> extractEvidence(String log, String keyword) {
        List<String> evidence = new ArrayList<>();
        String[] lines = log.split("\n");
        int count = 0;
        for (String line : lines) {
            if (line.contains(keyword) && count < 3) {
                evidence.add(line.substring(0, Math.min(200, line.length())));
                count++;
            }
        }
        return evidence;
    }
}
