package cn.jc.incident.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LokiQueryResult {

    private String logs;

    /**
     * ERROR 日志条数
     */
    private int errorCount;

    /**
     * 第一条 ERROR 日志位置
     */
    private String firstErrorPosition;

}