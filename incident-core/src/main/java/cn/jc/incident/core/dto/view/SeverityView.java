package cn.jc.incident.core.dto.view;

import lombok.Data;

@Data
public class SeverityView {

    private String level;          // P0 / P1 / P2 / P3
    private String label;          // Critical / High / Medium / Low
    private String color;          // red / orange / yellow / green
    private String description;    // 对人解释的严重性
}
