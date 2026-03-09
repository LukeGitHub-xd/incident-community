package cn.jc.incident.core.port.impl;

import cn.jc.incident.core.port.LogPreProcessor;
import cn.jc.incident.core.util.LogMasker;
import org.springframework.stereotype.Component;

@Component
public class SimpleLogPreProcessor implements LogPreProcessor {
    @Override
    public String preprocess(String rawLog) {
        if (rawLog == null) return "";
        return LogMasker.mask(
                rawLog.trim()
                        .replaceAll("\\s+", " ")
                        .replaceAll("[^\\x20-\\x7E]", "")
        );
    }
}