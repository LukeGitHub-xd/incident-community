package cn.jc.incident.core.port.impl;

import cn.jc.incident.core.port.LogPreProcessor;
import cn.jc.incident.core.util.LogMasker;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
public class DefaultLogPreProcessor implements LogPreProcessor {

    private static final int MAX_TOTAL_LINES = 200;
    private int contextLines = 5;

    private static final List<String> ERROR_KEYWORDS = List.of(
            "ERROR", "Exception", "FATAL", "Caused by"
    );

    @Override
    public String preprocess(String rawLog) {
        if (rawLog == null || rawLog.isBlank()) {
            return "";
        }

        List<String> lines = rawLog.lines().toList();
        Set<Integer> pickedIndexes = new LinkedHashSet<>();

        for (int i = 0; i < lines.size(); i++) {
            if (containsError(lines.get(i))) {
                pickContext(lines, i, pickedIndexes);
            }
        }

        if (pickedIndexes.isEmpty()) {
            int start = Math.max(0, lines.size() - MAX_TOTAL_LINES);
            for (int i = start; i < lines.size(); i++) {
                pickedIndexes.add(i);
            }
        }

        List<String> result = pickedIndexes.stream()
                .sorted()
                .limit(MAX_TOTAL_LINES)
                .map(l -> LogMasker.mask(lines.get(l)))
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("=== FILTERED LOG (ERROR CONTEXT) ===\n");
        result.forEach(l -> sb.append(l).append("\n"));
        return sb.toString();
    }

    private boolean containsError(String line) {
        return ERROR_KEYWORDS.stream().anyMatch(line::contains);
    }

    private void pickContext(List<String> lines, int errorIndex, Set<Integer> picked) {
        int start = Math.max(0, errorIndex - contextLines);
        int end = Math.min(lines.size() - 1, errorIndex + contextLines);
        for (int i = start; i <= end; i++) {
            picked.add(i);
        }
    }
}
