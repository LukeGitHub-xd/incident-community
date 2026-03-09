package cn.jc.incident.core.rule.model;

import lombok.Data;

import java.util.List;

@Data
public class MatchScope {

    /**
     * 支持通配：payment-* , *
     */
    private List<String> services;

    /**
     * 支持正则
     */
    private String servicesRegex;
}
