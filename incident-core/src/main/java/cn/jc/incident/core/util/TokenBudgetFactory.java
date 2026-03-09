package cn.jc.incident.core.util;

import cn.jc.incident.core.model.TokenBudget;
import org.springframework.stereotype.Component;

/**
 * Token 预算工厂
 * 决定：一次事故最多消耗多少 AI 成本
 */
@Component
public class TokenBudgetFactory {

    public TokenBudget create(String budgetLevel) {

        // 默认 Free
        if (budgetLevel == null) {
            return TokenBudget.free();
        }

        return switch (budgetLevel.toUpperCase()) {
            case "PRO" -> TokenBudget.pro();
            case "ENTERPRISE" -> TokenBudget.enterprise();
            default -> TokenBudget.free();
        };
    }
}
