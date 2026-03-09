package cn.jc.incident.core.mapper;

import cn.jc.incident.core.model.*;
import cn.jc.incident.infrastructure.persistence.entity.IncidentEntity;
import cn.jc.incident.infrastructure.persistence.json.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IncidentMapper {

    private final ObjectMapper objectMapper;

    public IncidentMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public IncidentEntity toEntity(Incident incident) {
        IncidentEntity e = new IncidentEntity();
        e.setId(incident.getId());
        e.setServiceName(incident.getServiceName());
        e.setEnv(incident.getEnv());
        e.setOccurTime(incident.getOccurTime());
        e.setChangeSummary(incident.getChangeSummary());
        e.setRawLog(incident.getRawLog());
        e.setSummary(incident.getSummary());
        e.setSeverityLevel(
                incident.getSeverityLevel() != null
                        ? incident.getSeverityLevel().name()
                        : null
        );

        e.setUserImpact(incident.isUserImpact());
        e.setTokenUsage(incident.getTokenUsage());

        // ✅ 直接赋值 DTO 对象，不用 ObjectMapper
        List<ConfirmedFindingJson> confirmed =
                incident.getConfirmedRootCauses()
                        .stream()
                        .map(desc -> new ConfirmedFindingJson(desc, List.of())) // 如果没有 evidence，可以传空列表
                        .toList();
        e.setConfirmedRootCauses(confirmed);

        List<ConfirmedFindingJson> suspected =
                incident.getSuspectedRootCauses()
                        .stream()
                        .map(desc -> new ConfirmedFindingJson(desc, List.of()))
                        .toList();
        e.setSuspectedRootCauses(suspected);

        e.setUncertainties(incident.getUncertainties());
        e.setRecommendations(incident.getRecommendations());

        // SeverityDecision
        e.setSeverityDecisions(incident.getSeverityDecisions()
                .stream()
                .map(sd -> new SeverityDecisionJson(sd.getSource().name(),sd.getScorer(), sd.getLevel().name(), sd.getReason(), sd.getWeight()))
                .findFirst()
                .orElse(null)
        );
// 报告结构
        e.setTimeline(convertToJsonList(incident.getTimeline(), TimelineEventJson.class));
        e.setErrorTrend(convertToJsonList(incident.getErrorTrend(), TrendPointJson.class));
        e.setServiceImpact(convertToJsonList(incident.getServiceImpact(), ServiceImpactJson.class));
        e.setImmediateActions(incident.getImmediateActions());
        e.setShortTermImprovements(incident.getShortTermImprovements());
        e.setLongTermImprovements(incident.getLongTermImprovements());
        return e;

    }

    public Incident toDomain(IncidentEntity e) {
        try {
            Incident incident = Incident.builder()
                    .id(e.getId())
                    .serviceName(e.getServiceName())
                    .env(e.getEnv())
                    .occurTime(e.getOccurTime())
                    .changeSummary(e.getChangeSummary())
                    .rawLog(e.getRawLog())
                    .summary(e.getSummary())
                    .severityLevel(
                            e.getSeverityLevel() != null
                                    ? SeverityLevel.valueOf(e.getSeverityLevel())
                                    : null
                    )
                    .userImpact(Boolean.TRUE.equals(e.getUserImpact()))
                    .tokenUsage(e.getTokenUsage() != null ? e.getTokenUsage() : 0)
                    .build();

            // 正确的反序列化方式 - 使用 TypeReference 提供泛型信息
            if (e.getConfirmedRootCauses() != null) {
                List<ConfirmedFindingJson> confirmedFindingJsons = objectMapper.readValue(
                        objectMapper.writeValueAsString(e.getConfirmedRootCauses()),
                        new TypeReference<List<ConfirmedFindingJson>>() {}
                );
                incident.setConfirmedRootCauses(
                        confirmedFindingJsons.stream()
                                .map(ConfirmedFindingJson::getDescription)
                                .collect(Collectors.toList())
                );
            } else {
                incident.setConfirmedRootCauses(new ArrayList<>());
            }

            if (e.getSuspectedRootCauses() != null) {
                List<ConfirmedFindingJson> suspectedFindingJsons = objectMapper.readValue(
                        objectMapper.writeValueAsString(e.getSuspectedRootCauses()),
                        new TypeReference<List<ConfirmedFindingJson>>() {}
                );
                incident.setSuspectedRootCauses(
                        suspectedFindingJsons.stream()
                                .map(ConfirmedFindingJson::getDescription)
                                .collect(Collectors.toList())
                );
            } else {
                incident.setSuspectedRootCauses(new ArrayList<>());
            }

            // Uncertainties 和 Recommendations 是简单的字符串列表，可以直接处理
            incident.setUncertainties(e.getUncertainties() != null ? e.getUncertainties() : new ArrayList<>());
            incident.setRecommendations(e.getRecommendations() != null ? e.getRecommendations() : new ArrayList<>());

            // SeverityDecisions 处理
            if (e.getSeverityDecisions() != null) {
                SeverityDecisionJson sdJson = e.getSeverityDecisions();
                SeverityDecision sd = new SeverityDecision(
                        SeverityDecisionSource.valueOf(sdJson.getSource()),
                        sdJson.getScorer(),
                        SeverityLevel.valueOf(sdJson.getLevel().replace(" ", "")), // 移除空格以匹配枚举
                        sdJson.getReason(),
                        sdJson.getWeight()
                );
                incident.setSeverityDecisions(List.of(sd));
            } else {
                incident.setSeverityDecisions(new ArrayList<>());
            }
            incident.setTimeline(
                    convertToDomainList(e.getTimeline(), TimelineEvent.class)
            );

            incident.setErrorTrend(
                    convertToDomainList(e.getErrorTrend(), TrendPoint.class)
            );

            incident.setServiceImpact(
                    convertToDomainList(e.getServiceImpact(), ServiceImpact.class)
            );

            incident.setImmediateActions(
                    e.getImmediateActions() != null ? e.getImmediateActions() : new ArrayList<>()
            );

            incident.setShortTermImprovements(
                    e.getShortTermImprovements() != null ? e.getShortTermImprovements() : new ArrayList<>()
            );

            incident.setLongTermImprovements(
                    e.getLongTermImprovements() != null ? e.getLongTermImprovements() : new ArrayList<>()
            );
            return incident;

        } catch (Exception ex) {
            throw new RuntimeException("Incident reverse mapping failed", ex);
        }
    }

    private <T, R> R convertToDto(T source, Class<R> targetClass) {
        if (source == null) {
            return null;
        }
        return objectMapper.convertValue(source, targetClass);
    }

    private <T, R> List<R> convertToJsonList(List<T> sourceList, Class<R> targetClass) {
        if (sourceList == null) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(item -> convertToDto(item, targetClass))
                .collect(Collectors.toList());
    }

    private <T, R> List<R> convertToDomainList(List<T> sourceList, Class<R> targetClass) {
        if (sourceList == null) {
            return new ArrayList<>();
        }
        return sourceList.stream()
                .map(item -> objectMapper.convertValue(item, targetClass))
                .collect(Collectors.toList());
    }
}
