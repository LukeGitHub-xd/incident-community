package cn.jc.incident.core.port;

import cn.jc.incident.core.model.TokenBudget;

public interface LogPreProcessor {
    String preprocess(String rawLog);

    default String preprocess(String rawLog, TokenBudget budget) {
        return preprocess(rawLog);
    }
}