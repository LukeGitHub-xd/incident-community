package cn.jc.incident.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuspectedFinding {

    /** 推测结论 */
    private String description;

    /** 推测依据 */
    private String reasoning;
}
