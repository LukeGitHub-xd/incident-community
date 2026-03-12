package cn.jc.incident.core.service.impl;

import cn.jc.incident.core.analysis.RootCauseAnalyzer;
import cn.jc.incident.core.dto.assembler.IncidentInsightAssembler;
import cn.jc.incident.core.dto.request.CreateIncidentRequest;
import cn.jc.incident.core.dto.response.ExecutiveSummary;
import cn.jc.incident.core.dto.response.IncidentInsightResponse;
import cn.jc.incident.core.dto.view.IncidentReportView;
import cn.jc.incident.core.dto.view.RootCauseOverview;
import cn.jc.incident.core.model.*;
import cn.jc.incident.core.port.LogPreProcessor;
import cn.jc.incident.core.repository.IncidentRepository;
import cn.jc.incident.core.service.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final LogPreProcessor logPreProcessor;
    private final RootCauseAnalyzer rootCauseAnalyzer;
    private final IncidentInsightAssembler insightAssembler;
    private final IncidentRepository incidentRepository;

    private static final Pattern TIME_PATTERN =
            Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");

    @Override
    public IncidentInsight analyze(CreateIncidentRequest request, TokenBudget budget) {
        // 开源简化版：只进行基础日志分析，不使用 AI
        Incident incident = Incident.create(request);
        String rawLog = incident.getRawLog();
            
        // 预处理日志
        String cleanedLog = logPreProcessor.preprocess(rawLog, budget);
            
        // 基础规则分析
        analyzeWithRules(cleanedLog, incident);
            
        // 构建报告增强内容
        fillMissingSections(cleanedLog, incident);
            
        // 保存事故记录
        // ✅ 内存存储
        incidentRepository.save(incident);
        return insightAssembler.assemble(incident);
    }
        
    /**
     * 基于简单规则的分析（开源版核心）
     */
    private void analyzeWithRules(String logs, Incident incident) {
        if (logs == null || logs.isBlank()) {
            return;
        }
            
        // 提取根因信号
        extractRootCauseSignals(logs, incident);
            
        // 设置默认严重级别
        incident.setSeverityLevel(SeverityLevel.P3);
        incident.setUserImpact(false);
    }

    /**
     * ⭐报告增强逻辑
     */
    private void fillMissingSections(String logs, Incident incident) {

        if (logs == null) {
            return;
        }
        // ---------- 三、疑似根因 ----------

        if (incident.getSuspectedRootCauses() == null
                || incident.getSuspectedRootCauses().isEmpty()) {

            List<String> suspected = new ArrayList<>();

            if (!logs.contains("Caused by")) {

                suspected.add("未发现完整异常链，可能存在隐藏根因");

            }

            if (!logs.contains("Exception")) {

                suspected.add("日志未包含异常类型，可能日志级别不足");

            }

            if (suspected.isEmpty()) {

                suspected.add("当前日志未发现明显次级异常");

            }

            incident.setSuspectedRootCauses(suspected);
        }


        // ---------- 六、短期改进 ----------

        if (incident.getShortTermImprovements() == null
                || incident.getShortTermImprovements().isEmpty()) {

            List<String> shortTerm = new ArrayList<>();

            shortTerm.add("增加业务异常日志记录");

            shortTerm.add("为关键接口增加错误率监控");

            if (logs.contains("BusinessException")) {

                shortTerm.add("为业务异常增加统一 error code 与日志说明");
            }

            incident.setShortTermImprovements(shortTerm);
        }


        // ---------- 七、长期优化 ----------

        if (incident.getLongTermImprovements() == null
                || incident.getLongTermImprovements().isEmpty()) {

            List<String> longTerm = new ArrayList<>();

            longTerm.add("建立统一业务异常规范");

            longTerm.add("完善服务 SLA 与异常监控");

            longTerm.add("增加异常链路日志记录（stacktrace）");

            incident.setLongTermImprovements(longTerm);
        }
        List<String> suspected = buildSuspectedRootCauses(logs);

        if (!suspected.isEmpty()) {
            incident.setSuspectedRootCauses(suspected);
        }
        List<String> uncertainties = buildUncertainties(logs);

        if (!uncertainties.isEmpty()) {
            incident.setUncertainties(uncertainties);
        }
        buildTimeline(logs, incident);

        buildErrorTrend(logs, incident);

        buildServiceImpact(logs, incident);
        List<String> shortTerm = buildShortTermImprovements(logs);

        if (!shortTerm.isEmpty()) {
            incident.setShortTermImprovements(shortTerm);
        }
        List<String> longTerm = buildLongTermOptimizations(logs);

        if (!longTerm.isEmpty()) {
            incident.setLongTermImprovements(longTerm);
        }
    }
    private List<String> buildLongTermOptimizations(String logs) {

        List<String> optimizations = new ArrayList<>();

        if (logs == null) {
            return optimizations;
        }

        if (logs.contains("BusinessException")) {
            optimizations.add("业务异常");
        }

        if (logs.contains("Timeout")) {
            optimizations.add("引入下游服务熔断与降级机制");
        }

        if (logs.contains("NullPointerException")) {
            optimizations.add("加强代码质量检查，增加静态分析");
        }

        if (logs.contains("OutOfMemoryError")) {
            optimizations.add("优化 JVM 内存配置并引入内存监控");
        }

        return optimizations;
    }
    private List<String> buildShortTermImprovements(String logs) {

        List<String> improvements = new ArrayList<>();

        if (logs == null) {
            return improvements;
        }

        if (logs.contains("BusinessException")) {
            improvements.add("增加业务异常日志记录，便于定位问题");
        }

        if (logs.contains("NullPointerException")) {
            improvements.add("增加关键对象非空校验");
        }

        if (logs.contains("Timeout")) {
            improvements.add("增加下游服务调用超时日志与监控");
        }

        if (logs.contains("ERROR")) {
            improvements.add("为关键接口增加错误率监控");
        }

        return improvements;
    }
    private List<String> buildUncertainties(String logs) {

        List<String> uncertainties = new ArrayList<>();

        if (logs == null || logs.isEmpty()) {

            uncertainties.add("日志为空，无法确认完整异常链");

            return uncertainties;
        }

        if (!logs.contains("Caused by")) {

            uncertainties.add("未发现完整异常链 (Caused by)");
        }

        if (!logs.contains("StackTrace")) {

            uncertainties.add("日志未包含完整堆栈信息");
        }

        if (logs.length() < 500) {

            uncertainties.add("日志样本较少，分析可能不完整");
        }

        return uncertainties;
    }
    private List<String> buildSuspectedRootCauses(String logs) {

        List<String> suspects = new ArrayList<>();

        if (logs == null || logs.isEmpty()) {
            return suspects;
        }

        String[] lines = logs.split("\n");

        for (String line : lines) {

            if (line.contains("Caused by")) {
                suspects.add(line.trim());
            }

            else if (line.contains("Timeout")) {
                suspects.add("Possible downstream timeout: " + line.trim());
            }

            else if (line.contains("NullPointerException")) {
                suspects.add("Possible null pointer issue");
            }

            else if (line.contains("OutOfMemoryError")) {
                suspects.add("Possible JVM memory exhaustion");
            }

            if (suspects.size() >= 3) {
                break;
            }
        }

        return suspects;
    }

    /**
     * 1️⃣ 构建事故时间线
     */
    private void buildTimeline(String logs, Incident incident) {

        if (incident.getTimeline() != null
                && !incident.getTimeline().isEmpty()) {
            return;
        }

        List<TimelineEvent> timeline = new ArrayList<>();

        String[] lines = logs.split("\n");

        for (String line : lines) {

            if (!(line.contains("ERROR")
                    || line.contains("Exception")
                    || line.contains("BusinessException"))) {
                continue;
            }

            String time = extractTime(line);

            TimelineEvent event = new TimelineEvent();

            event.setTime(time != null ? time : "-");

            event.setEvent(
                    line.length() > 160
                            ? line.substring(0, 160) + "..."
                            : line
            );

            event.setSource("LOG");

            timeline.add(event);

            if (timeline.size() >= 6) {
                break;
            }
        }

        if (timeline.isEmpty()) {

            TimelineEvent event = new TimelineEvent();

            event.setTime("-");

            event.setEvent("暂无关键异常事件");

            event.setSource("-");

            timeline.add(event);
        }

        incident.setTimeline(timeline);
    }

    /**
     * 2️⃣ 构建异常趋势（分钟级）
     */
    private void buildErrorTrend(String logs, Incident incident) {

        if (incident.getErrorTrend() != null
                && !incident.getErrorTrend().isEmpty()) {
            return;
        }

        Map<String, Integer> counter = new TreeMap<>();

        String[] lines = logs.split("\n");

        for (String line : lines) {

            if (!(line.contains("ERROR")
                    || line.contains("Exception")
                    || line.contains("BusinessException"))) {
                continue;
            }

            String time = extractTime(line);

            if (time == null) {
                continue;
            }

            String minute = time.substring(11, 16);

            counter.merge(minute, 1, Integer::sum);
        }

        List<TrendPoint> trend = new ArrayList<>();

        for (var entry : counter.entrySet()) {

            TrendPoint p = new TrendPoint();

            p.setMinute(entry.getKey());

            p.setErrorCount(entry.getValue());

            trend.add(p);
        }

        if (trend.isEmpty()) {

            TrendPoint p = new TrendPoint();

            p.setMinute(
                    LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("HH:mm"))
            );

            p.setErrorCount(0);

            trend.add(p);
        }

        incident.setErrorTrend(trend);
    }

    /**
     * 3️⃣ 构建服务影响
     */
    private void buildServiceImpact(String logs, Incident incident) {

        if (incident.getServiceImpact() != null
                && !incident.getServiceImpact().isEmpty()) {
            return;
        }

        int errorCount =
                (int) Arrays.stream(logs.split("\n"))
                        .filter(l -> l.contains("ERROR"))
                        .count();

        ServiceImpact impact = new ServiceImpact();

        impact.setService(
                incident.getServiceName() != null
                        ? incident.getServiceName()
                        : "UNKNOWN"
        );

        if (errorCount > 200) {
            impact.setLevel("HIGH");
        } else if (errorCount > 50) {
            impact.setLevel("MEDIUM");
        } else {
            impact.setLevel("LOW");
        }

        incident.setServiceImpact(List.of(impact));
    }

    /**
     * 提取日志时间
     */
    private String extractTime(String line) {

        Matcher matcher = TIME_PATTERN.matcher(line);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * RootCause信号提取
     */
    private void extractRootCauseSignals(String log, Incident incident) {

        if (log == null) {
            return;
        }

        String rootCause =
                rootCauseAnalyzer.extractRootCause(log);

        String location =
                rootCauseAnalyzer.extractErrorLocation(log);

        String evidence =
                rootCauseAnalyzer.extractEvidence(log);

        List<String> confirmed = new ArrayList<>();
        List<String> suspects = new ArrayList<>();

        if (!"UNKNOWN".equals(rootCause)) {
            confirmed.add(rootCause);
        }

        if (log.contains("NullPointerException")) {
            suspects.add("Null pointer usage detected");
        }

        if (log.contains("OutOfMemoryError")) {
            suspects.add("JVM memory exhaustion");
        }

        if (log.contains("Timeout")) {
            suspects.add("Downstream timeout");
        }

        incident.setConfirmedRootCauses(confirmed);

        incident.setSuspectedRootCauses(suspects);

        incident.setEvidence(evidence);

        incident.setErrorLocation(location);
    }

    @Override
    public IncidentInsightResponse getInsight(String incidentId) {
        var incident = incidentRepository.findById(incidentId).orElseThrow(() -> new IllegalArgumentException("Incident not found"));
        IncidentInsight insight = insightAssembler.assemble(incident);
        return IncidentInsightResponse.from(incidentId, insight);
    }

    @Override
    public IncidentReportView getIncidentReport(String incidentId, ReportPlan plan) {
        // 开源版统一返回标准报告，不区分版本策略
        var incident = incidentRepository.findById(incidentId).orElseThrow(() -> new IllegalArgumentException("Incident not found"));
        IncidentReportView view = new IncidentReportView();
        
        // 填充基本报告内容
        view.setExecutiveSummary(buildExecutiveSummary(incident));
        view.setRootCause(buildRootCauseOverview(incident));
        view.setTimeline(incident.getTimeline());
        view.setErrorTrend(incident.getErrorTrend());
        view.setServiceImpact(incident.getServiceImpact());
        
        return view;
    }
    
    private ExecutiveSummary buildExecutiveSummary(Incident incident) {
        ExecutiveSummary summary = new ExecutiveSummary();
        summary.setHeadline("日志分析报告 - " + incident.getServiceName());
        summary.setSeverity(incident.getSeverityLevel() != null ? incident.getSeverityLevel() : SeverityLevel.P3);
        summary.setStatus(IncidentStatus.RESOLVED);
        return summary;
    }
    
    private RootCauseOverview buildRootCauseOverview(Incident incident) {
        RootCauseOverview overview = new RootCauseOverview();
        overview.setConfirmedRootCauses(incident.getConfirmedRootCauses());
        overview.setSuspectedRootCauses(incident.getSuspectedRootCauses());
        overview.setUncertainties(new ArrayList<>());
        return overview;
    }

    public IncidentReportRenderModel generateReport(String incidentId) {

        IncidentReportView reportView = getIncidentReport(incidentId, ReportPlan.FREE);
        
        return new IncidentReportRenderModel(
                reportView,
                incidentId,
                LocalDateTime.now()
        );
    }

}