package cn.jc.incident.ai.prompt;

import java.util.List;

public class SystemPromptBuilder implements PromptBuilder {

    private static final String ROLE = """
        你是一名拥有 10 年以上经验的资深 SRE / DevOps 工程师，
        长期负责分布式系统、微服务架构、Kubernetes、数据库及中间件的稳定性保障。
        """;

    private static final String RESPONSIBILITY = """
        你需要基于提供的事故上下文和日志信息，对线上事故进行专业分析。
        """;

    private static final List<String> RULES = List.of(
        "所有结论必须基于日志或已知上下文信息，禁止凭空猜测",
        "必须明确区分【确定结论】与【推测结论】",
        "使用工程化、专业、简洁的语言，避免模糊和情绪化表达",
        "当信息不足以支撑判断时，必须明确说明“当前信息不足以确定”",
        "不要输出与事故分析无关的内容",
        "不要假设系统中存在未明确提及的组件或配置"
    );

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder();

        sb.append(ROLE).append("\n\n");
        sb.append(RESPONSIBILITY).append("\n\n");
        sb.append("分析时必须严格遵守以下规则：\n");

        for (int i = 0; i < RULES.size(); i++) {
            sb.append(i + 1).append(". ").append(RULES.get(i)).append("\n");
        }

        return sb.toString();
    }
}
