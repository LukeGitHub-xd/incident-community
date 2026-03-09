package cn.jc.incident.core.port.impl;

import cn.jc.incident.core.model.TokenBudget;
import cn.jc.incident.core.port.LogPreProcessor;
import cn.jc.incident.core.util.LogMasker;
import cn.jc.incident.core.util.TokenEstimator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 预算感知日志预处理器
 * | 版本         | 特性               |
 * | ---------- | ---------------- |
 * | Free       | 固定 200 行         |
 * | Pro        | Token 自适应裁剪      |
 * | Enterprise | 多模型预算 + Trace 聚合 |
 *
 * @author xuedongdong
 * @date 2026/01/23
 */
@Component
public class BudgetAwareLogPreProcessor implements LogPreProcessor {

    private static final int CONTEXT_LINES = 5;

    private static final List<String> ERROR_KEYWORDS = List.of(
            "ERROR", "Exception", "FATAL", "Caused by"
    );

    @Override
    public String preprocess(String rawLog) {
        return "";
    }

    @Override
    public String preprocess(String rawLog, TokenBudget budget) {
        if (rawLog == null || rawLog.isBlank()) {
            return "";
        }

        List<String> lines = rawLog.lines().toList();
        StringBuilder result = new StringBuilder();

        result.append("=== FILTERED LOG (TOKEN BUDGET MODE) ===\n");

        int usedTokens = 0;

        for (int i = 0; i < lines.size(); i++) {
            if (containsError(lines.get(i))) {
                int start = Math.max(0, i - CONTEXT_LINES);
                int end = Math.min(lines.size() - 1, i + CONTEXT_LINES);

                for (int j = start; j <= end; j++) {
//                    String line = lines.get(j);
                    String line = LogMasker.mask(lines.get(j));
                    int tokens = TokenEstimator.estimate(line);

                    if (usedTokens + tokens > budget.availableForLog()) {
                        result.append("... [TRUNCATED DUE TO TOKEN LIMIT]\n");
                        return result.toString();
                    }

                    result.append(line).append("\n");
                    usedTokens += tokens;
                }
            }
        }

        // 如果完全没命中 ERROR，兜底从尾部反向塞
        if (usedTokens == 0) {
            for (int i = lines.size() - 1; i >= 0; i--) {
                String line = lines.get(i);
                int tokens = TokenEstimator.estimate(line);

                if (usedTokens + tokens > budget.availableForLog()) {
                    break;
                }

                result.insert(result.indexOf("\n") + 1, line + "\n");
                usedTokens += tokens;
            }
        }

        return result.toString();
    }

    private boolean containsError(String line) {
        return ERROR_KEYWORDS.stream().anyMatch(line::contains);
    }
}
