package cn.jc.incident.core.model;

/**
 *
 *
 * @author xuedongdong
 * @date 2026/01/24
 */
public enum SeverityLevel {
    P0("全站不可用 / 数据丢失"),
    P1("核心功能受损"),
    P2("局部异常 / 可降级"),
    P3("风险 / 潜在问题");

    private final String description;

    SeverityLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
