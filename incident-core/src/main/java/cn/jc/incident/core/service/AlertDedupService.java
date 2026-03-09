package cn.jc.incident.core.service;

public interface AlertDedupService {
    boolean shouldProcess(String key);
}
