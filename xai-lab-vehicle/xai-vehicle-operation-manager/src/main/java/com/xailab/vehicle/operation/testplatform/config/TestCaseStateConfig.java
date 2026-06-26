package com.xailab.vehicle.operation.testplatform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: TestCaseStateConfig
 * @Description:
 * @author: liulin
 * @date: 2025/7/20 17:31
 */
@Configuration
@ConfigurationProperties(prefix = "test-case-state.task")
@Data
public class TestCaseStateConfig {

    /**
     * 任务是否启用
     */
    private Boolean taskEnable =true;


}
