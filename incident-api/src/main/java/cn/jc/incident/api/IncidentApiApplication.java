package cn.jc.incident.api;

import cn.jc.incident.core.config.RuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication(scanBasePackages = {"cn.jc.incident"})
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(RuleProperties.class)
public class IncidentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IncidentApiApplication.class, args);
        log.info("IncidentApiApplication started successfully.");
    }

}
