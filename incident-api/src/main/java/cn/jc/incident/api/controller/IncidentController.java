package cn.jc.incident.api.controller;// ... existing code ...

import cn.jc.incident.core.dto.request.CreateIncidentRequest;
import cn.jc.incident.core.dto.response.IncidentInsightResponse;
import cn.jc.incident.core.dto.view.IncidentReportView;
import cn.jc.incident.core.model.IncidentInsight;
import cn.jc.incident.core.model.IncidentReportRenderModel;
import cn.jc.incident.core.model.ReportPlan;
import cn.jc.incident.core.model.TokenBudget;
import cn.jc.incident.core.report.IncidentReportExporter;
import cn.jc.incident.core.report.renderer.HtmlReportRenderer;
import cn.jc.incident.core.report.renderer.MarkdownReportRenderer;
import cn.jc.incident.core.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
// ... existing code ...

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentController {

    private final IncidentService incidentService;
    private final MarkdownReportRenderer markdownRenderer;
    private final HtmlReportRenderer htmlRenderer;
    private final IncidentReportExporter reportExporter;

    /**
     * 【开源版核心功能】上传日志文件进行分析
     */
    @PostMapping("/upload")
    public IncidentInsight uploadLogFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("serviceName") String serviceName,
            @RequestParam(value = "env", defaultValue = "production") String env) throws IOException {
        
        String logContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        
        CreateIncidentRequest request = new CreateIncidentRequest();
        request.setServiceName(serviceName);
        request.setEnv(env);
        request.setLogContent(logContent);
        request.setChangeSummary("开源版 - 手动上传日志分析");
        
        return incidentService.analyze(request, TokenBudget.free());
    }

    /**
     * 文本日志快速分析
     */
    @PostMapping("/analyze")
    public IncidentInsight analyzeTextLog(
            @RequestParam("log") String logContent,
            @RequestParam("serviceName") String serviceName,
            @RequestParam(value = "env", defaultValue = "production") String env) {
        
        CreateIncidentRequest request = new CreateIncidentRequest();
        request.setServiceName(serviceName);
        request.setEnv(env);
        request.setLogContent(logContent);
        request.setChangeSummary("开源版 - 文本日志分析");
        
        return incidentService.analyze(request, TokenBudget.free());
    }

    /**
     * 获取事故洞察
     */
    @GetMapping("/{incidentId}/insight")
    public IncidentInsightResponse insight(@PathVariable String incidentId) {
        return incidentService.getInsight(incidentId);
    }

    /**
     * 获取事故报告视图
     */
    @GetMapping("/{id}/report")
    public IncidentReportView getIncidentReport(
            @PathVariable String id) {
        return incidentService.getIncidentReport(id, ReportPlan.FREE);
    }

    /**
     * 下载报告文件（Markdown 格式 - 开源版限定）
     */
    @GetMapping("/{id}/report/file")
    public ResponseEntity<String> getReportFile(
            @PathVariable String id) {
        
        IncidentReportRenderModel renderModel = incidentService.generateReport(id);
        String markdown = markdownRenderer.render(renderModel.getReport());
        
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_MARKDOWN)
                .body(markdown);
    }

    /**
     * 下载 HTML 报告
     */
    @GetMapping("/{id}/report.html")
    public ResponseEntity<String> getHtmlReport(
            @PathVariable String id) {
        
        IncidentReportRenderModel renderModel = incidentService.generateReport(id);
        String markdown = markdownRenderer.render(renderModel.getReport());
        String html = htmlRenderer.render(markdown);
        
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    /**
     * 下载 PDF 报告
     */
    @GetMapping(value = "/{id}/report.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable String id) {
        
        IncidentReportRenderModel model = incidentService.generateReport(id);
        byte[] pdf = reportExporter.exportPdf(model, Locale.CHINA);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=incident-" + id + ".pdf")
                .body(pdf);
    }

}
