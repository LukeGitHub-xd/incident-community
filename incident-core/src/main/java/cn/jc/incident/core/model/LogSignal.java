package cn.jc.incident.core.model;

import lombok.Data;

@Data
public class LogSignal {

    private String exceptionType;

    private String message;

    private String stackTop;

    private String evidence;

}