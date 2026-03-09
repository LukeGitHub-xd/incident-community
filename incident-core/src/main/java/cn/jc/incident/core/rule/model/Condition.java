package cn.jc.incident.core.rule.model;

import lombok.Data;

@Data
public class Condition {

    private String field;
    private String op;     // eq / gt / lt / contains
    private Object value;
}
