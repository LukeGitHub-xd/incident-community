package cn.jc.incident.core.service.impl;

import cn.jc.incident.core.service.AlertDedupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Profile("prod")
@Service
@RequiredArgsConstructor
public class AlertDedupServiceImpl implements AlertDedupService {


    private final Map<String, Long> cache = new ConcurrentHashMap<>();

    @Override
    public boolean shouldProcess(String key) {

        // ✅ 生产逻辑
        long now = System.currentTimeMillis();
        Long last = cache.get(key);

        log.info("Dedup check: key={}, last={}, now={}", key, last, now);

        if (last == null || now - last > 5 * 60 * 1000) {
            cache.put(key, now);
            log.info("Dedup passed: key={}", key);
            return true;
        }

        log.info("Dedup blocked: key={}", key);
        return false;
    }
}
