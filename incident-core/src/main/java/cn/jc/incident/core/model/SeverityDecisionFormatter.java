package cn.jc.incident.core.model;

import java.util.ArrayList;
import java.util.List;

public class SeverityDecisionFormatter {
    public static String format(SeverityDecision decision) {
        return String.format("[%s][%s] %s → %s",
                decision.getSource(),
                decision.getScorer(),
                decision.getLevel(),
                decision.getReason(),
                decision.getWeight());
    }

    public static List<String> formatAll(List<SeverityDecision> decisions) {
        List<String> formatted = new ArrayList<>();
        for (SeverityDecision d : decisions) {
            formatted.add(format(d));
        }
        return formatted;
    }
}

