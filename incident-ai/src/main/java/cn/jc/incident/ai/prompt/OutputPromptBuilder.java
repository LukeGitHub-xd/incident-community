package cn.jc.incident.ai.prompt;

public class OutputPromptBuilder implements PromptBuilder {

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();

        sb.append("请严格按照以下 JSON 结构输出事故分析结果。\n");
        sb.append("输出必须是【合法 JSON】，禁止包含任何额外说明性文字。\n");
        sb.append("禁止使用 Markdown、代码块或自然语言解释。\n\n");

        sb.append("输出结构如下：\n\n");

        sb.append("{\n");
        sb.append("  \"summary\": \"事故整体分析摘要（不超过 3 句话）\",\n");
        sb.append("  \"confirmed_findings\": [\n");
        sb.append("    {\n");
        sb.append("      \"description\": \"基于日志或已知事实可确认的结论\",\n");
        sb.append("      \"evidence\": [\n");
        sb.append("        \"支持该结论的日志或事实\"\n");
        sb.append("      ]\n");
        sb.append("    }\n");
        sb.append("  ],\n");
        sb.append("  \"suspected_findings\": [\n");
        sb.append("    {\n");
        sb.append("      \"description\": \"基于现象的合理推测结论\",\n");
        sb.append("      \"reasoning\": \"推测依据，必须基于已知信息\"\n");
        sb.append("    }\n");
        sb.append("  ],\n");
        sb.append("  \"uncertainties\": [\n");
        sb.append("    \"当前信息不足以确定的关键问题\"\n");
        sb.append("  ],\n");
        sb.append("  \"recommended_next_steps\": [\n");
        sb.append("    \"建议补充的日志、指标或验证动作\"\n");
        sb.append("  ]\n");
        sb.append("}\n\n");

        sb.append("重要约束：\n");
        sb.append("1. confirmed_findings 中的每一项必须有明确证据支持\n");
        sb.append("2. 不允许将推测内容放入 confirmed_findings\n");
        sb.append("3. 如果无法确认任何结论，confirmed_findings 必须为空数组\n");
        sb.append("4. 不确定的信息必须放入 uncertainties\n");
        sb.append("5. 不要输出与上述结构无关的字段\n");

        return sb.toString();
    }
}
