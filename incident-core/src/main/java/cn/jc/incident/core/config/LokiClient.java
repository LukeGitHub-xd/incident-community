package cn.jc.incident.core.config;

import cn.jc.incident.core.model.LokiQueryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LokiClient {

    private final RestTemplate restTemplate;
    private final LokiProperties properties;

    public LokiQueryResult queryRecentErrorLogs(
            String service,
            String env,
            int minutes) {

        try {

            String logql =
                    "{service_name=\"" + service + "\"} |~ \"Exception|ERROR\"";

            long end =
                    Instant.now().toEpochMilli() * 1_000_000;

            long start =
                    end - minutes * 60L * 1_000_000_000;

            String url =
                    properties.getBaseUrl()
                            + "/loki/api/v1/query_range"
                            + "?query=" + URLEncoder.encode(logql, StandardCharsets.UTF_8)
                            + "&start=" + start
                            + "&end=" + end
                            + "&limit=2000";

            URI uri = new URI(url);

            log.info("Loki query: {}", logql);
            log.info("Loki URL: {}", uri);
            log.info("Time range: start={}, end={}, minutes={}", start, end, minutes);

            Map response =
                    restTemplate.getForObject(uri, Map.class);

            if (response == null) {
                return new LokiQueryResult("", 0, null);
            }

            Map data =
                    (Map) response.get("data");

            if (data == null) {
                return new LokiQueryResult("", 0, null);
            }

            List<Map> result =
                    (List<Map>) data.get("result");

            if (result == null || result.isEmpty()) {
                return new LokiQueryResult("", 0, null);
            }

            StringBuilder logs = new StringBuilder();
            int errorCount = 0;
            String firstPosition = null;

            for (Map stream : result) {

                List<List<String>> values =
                        (List<List<String>>) stream.get("values");

                for (List<String> v : values) {

                    String timestamp = v.get(0);
                    String line = v.get(1);

                    // 🔥 去除 ANSI 颜色
                    line = line.replaceAll("\u001B\\[[;\\d]*m", "");

                    logs.append(line).append("\n");

                    errorCount++;

                    if (firstPosition == null) {
                        firstPosition = timestamp;
                    }
                }
            }

            return new LokiQueryResult(
                    logs.toString(),
                    errorCount,
                    firstPosition
            );

        } catch (Exception e) {

            log.error("Loki query failed", e);

            return new LokiQueryResult("", 0, null);
        }
    }
}