package cn.jc.incident.ai.prompt.model;

import lombok.Data;

import java.util.List;

/**
 * 事件背景
 *
 * @author xuedongdong
 * @date 2026/01/23
 */
@Data
public class IncidentContext {

    /** 事故简要描述 */
    private String incidentSummary;

    /** 事故发生时间范围 */
    private String timeRange;

    /** 涉及的系统 / 服务 */
    private List<String> involvedServices;

    /** 已知事实（人工或系统确认） */
    private List<String> knownFacts;

    /** 原始日志（可截断） */
    private List<String> logs;

    /** 监控与指标描述 */
    private List<String> metrics;

}
