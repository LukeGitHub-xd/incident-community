package cn.jc.incident.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedFinding {

    /** 确定结论描述 */
    private String description;

    /** 支撑该结论的证据（日志/事实） */
    private List<String> evidence;
}
