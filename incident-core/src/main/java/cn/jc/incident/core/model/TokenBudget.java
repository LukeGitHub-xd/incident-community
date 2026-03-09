package cn.jc.incident.core.model;

import lombok.Getter;

@Getter
public class TokenBudget {

    private final int total;
    private int used;

    private TokenBudget(int total) {
        this.total = total;
        this.used = 0;
    }
    /**
     * 🔑 统一入口（给 Controller / API 用）
     */
    public static TokenBudget from(String level) {
        if (level == null) {
            return free();
        }

        return switch (level.toUpperCase()) {
            case "PRO" -> pro();
            case "ENTERPRISE", "BUSINESS" -> enterprise();
            default -> free();
        };
    }

    public static TokenBudget free() {
        return new TokenBudget(1_500);
    }

    public static TokenBudget pro() {
        return new TokenBudget(4_000);
    }

    public static TokenBudget enterprise() {
        return new TokenBudget(12_000);
    }

    public int availableForLog() {
        // 给日志 60%
        return (int) (total * 0.6) - used;
    }

    public void consume(int tokens) {
        this.used += tokens;
    }

    public int currentUsage() {
        return used;
    }
}

