package cn.jc.incident.core.report;

import cn.jc.incident.core.model.IncidentReportRenderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class IncidentReportExporter {

    private final HtmlRenderService htmlRenderService;
    private final PdfRenderService pdfRenderService;

    public byte[] exportPdf(IncidentReportRenderModel model, Locale locale) {
        String template = Locale.CHINESE.getLanguage().equals(locale.getLanguage()) ?
                "incident-report-CN" : "incident-report";
        // 直接传 model 对象，不要包装！
        String html = htmlRenderService.render(template, model);
        return pdfRenderService.renderPdf(html);
    }

    public byte[] exportPdf(IncidentReportRenderModel model) {
        String html = htmlRenderService.render("incident-report", model);
        return pdfRenderService.renderPdf(html);
    }
}
