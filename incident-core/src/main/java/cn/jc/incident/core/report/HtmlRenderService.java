package cn.jc.incident.core.report;

public interface HtmlRenderService {

    String render(String template, Object model);
}
