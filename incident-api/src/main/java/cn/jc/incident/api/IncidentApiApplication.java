package cn.jc.incident.api;

import cn.jc.incident.core.config.RuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication(scanBasePackages = {"cn.jc.incident"})
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(RuleProperties.class)
@EnableJpaRepositories(
        basePackages = "cn.jc.incident.**.repository"
)
@EntityScan(
        basePackages = "cn.jc.incident.**.entity"
)
public class IncidentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IncidentApiApplication.class, args);
        log.info("IncidentApiApplication started successfully.");
    }

}
