package cn.jc.incident.core.dto.view;

import lombok.Data;

import java.util.List;

@Data
public class FindingView {

    private String title;           // 结论一句话
    private List<String> evidence;  // 证据（最多 3 条，已裁剪）
}
