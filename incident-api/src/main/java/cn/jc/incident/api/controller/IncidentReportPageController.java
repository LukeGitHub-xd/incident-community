package cn.jc.incident.api.controller;

import cn.jc.incident.core.model.IncidentReportRenderModel;
import cn.jc.incident.core.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class IncidentReportPageController {

    private final IncidentService incidentService;

    /**
     * 渲染报告
     *
     * @param id    ID
     * @param model 模型
     * @return {@link String }
     */
    @GetMapping("/{id}/report/html")
    public String renderReport(@PathVariable String id, Locale locale, Model model) {
        IncidentReportRenderModel report = incidentService.generateReport(id);
        model.addAttribute("report", report);
        // locale可以从请求头、Session或Cookie中自动解析
        return Locale.CHINESE.getLanguage().equals(locale.getLanguage()) ?
                "incident-report-CN" :
                "incident-report";
    }
}