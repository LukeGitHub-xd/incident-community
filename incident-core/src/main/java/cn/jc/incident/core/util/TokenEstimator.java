package cn.jc.incident.core.util;

public class TokenEstimator {

    /**
     * 粗略估算：英文 1 token ≈ 4 chars
     * 中文 1 token ≈ 1.5 chars（取 2 保守）
     */
    public static int estimate(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return Math.max(1, text.length() / 4);
    }
}
