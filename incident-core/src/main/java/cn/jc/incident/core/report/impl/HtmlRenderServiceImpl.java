package cn.jc.incident.core.report.impl;

import cn.jc.incident.core.report.HtmlRenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class HtmlRenderServiceImpl implements HtmlRenderService {

    private final SpringTemplateEngine templateEngine;

    @Override
    public String render(String template, Object model) {
        Context ctx = new Context();
        // 关键：把传入的 model（即 IncidentReportRenderModel）绑定到变量名 "report"
        ctx.setVariable("report", model);
        return templateEngine.process(template, ctx);
    }
}


