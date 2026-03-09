package cn.jc.incident.core.service.impl;

import cn.jc.incident.core.service.AlertDedupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Profile("!prod")
@Service
@RequiredArgsConstructor
public class NoopAlertDedupService implements AlertDedupService {


    private final Map<String, Long> cache = new ConcurrentHashMap<>();

    @Override
    public boolean shouldProcess(String key) {

        // ✅ 测试环境：直接放行
        log.info("Dedup disabled, always pass. key={}", key);
        return true;

    }
}
