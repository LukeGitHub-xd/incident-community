package cn.jc.incident.core.analysis;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RootCauseAnalyzer {

    private static final Pattern EXCEPTION_MESSAGE =
            Pattern.compile("([\\w\\.]+Exception):\\s*(.+)");

    private static final Pattern EXCEPTION_ONLY =
            Pattern.compile("([\\w\\.]+Exception)");

    private static final Pattern STACK_PATTERN =
            Pattern.compile("at\\s+([\\w\\.]+)\\(([^:]+:\\d+)\\)");

    /**
     * 提取 RootCause
     */
    public String extractRootCause(String logs) {

        if (logs == null || logs.isBlank()) {
            return "UNKNOWN";
        }

        Matcher m = EXCEPTION_MESSAGE.matcher(logs);

        if (m.find()) {
            return m.group(1) + ": " + m.group(2);
        }

        m = EXCEPTION_ONLY.matcher(logs);

        if (m.find()) {
            return m.group(1);
        }

        return "UNKNOWN";
    }

    /**
     * 新增：异常分类
     */
    public String classifyException(String logs) {

        if (logs == null) {
            return "UNKNOWN";
        }

        if (logs.contains("NullPointerException")) {
            return "CODE_BUG";
        }

        if (logs.contains("TimeoutException")
                || logs.contains("ReadTimeout")
                || logs.contains("SocketTimeout")) {
            return "DEPENDENCY_TIMEOUT";
        }

        if (logs.contains("ConnectException")
                || logs.contains("Connection refused")) {
            return "DEPENDENCY_DOWN";
        }

        if (logs.contains("OutOfMemoryError")) {
            return "RESOURCE_MEMORY";
        }

        if (logs.contains("Too many connections")) {
            return "RESOURCE_DB_POOL";
        }

        if (logs.contains("IllegalArgumentException")) {
            return "BAD_INPUT";
        }

        return "UNKNOWN";
    }

    /**
     * 提取 Exception Message
     */
    public String extractMessage(String logs) {

        if (logs == null) {
            return "";
        }

        Matcher m = EXCEPTION_MESSAGE.matcher(logs);

        if (m.find()) {
            return m.group(2);
        }

        return "";
    }

    /**
     * 提取异常发生位置
     */
    public String extractErrorLocation(String logs) {

        if (logs == null) {
            return "unknown";
        }

        Matcher m = STACK_PATTERN.matcher(logs);

        if (m.find()) {

            String classMethod = m.group(1);
            String fileLine = m.group(2);

            return classMethod + "(" + fileLine + ")";
        }

        return "unknown";
    }

    /**
     * 提取关键证据
     */
    public String extractEvidence(String logs) {

        if (logs == null) {
            return "";
        }

        String[] lines = logs.split("\n");

        StringBuilder evidence = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {

            if (lines[i].contains("ERROR")
                    || lines[i].contains("Exception")) {

                int start = Math.max(0, i - 3);
                int end = Math.min(lines.length, i + 4);

                for (int j = start; j < end; j++) {
                    evidence.append(lines[j]).append("\n");
                }

                break;
            }
        }

        return evidence.toString();
    }
}