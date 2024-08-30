package com.cong.middleware.flex.pool.sdk.config;

import com.alibaba.fastjson.JSON;
import com.cong.middleware.flex.pool.sdk.domain.FlexThreadPoolService;
import com.cong.middleware.flex.pool.sdk.domain.impl.FlexThreadPoolServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Flex Pool 自动配置 入口
 *
 * @author cong
 * @date 2024/08/30
 */
@Configuration
public class FlexThreadPoolAutoConfig {
    private final Logger logger = LoggerFactory.getLogger(FlexThreadPoolAutoConfig.class);

    private String applicationName;

    @Bean("flexThreadPollService")
    public FlexThreadPoolService flexThreadPollService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");

        if (StringUtils.isBlank(applicationName)) {
            applicationName = "无法找到应用名称";
            logger.warn("动态线程池，启动提示。SpringBoot 应用未配置 spring.application.name 无法获取到应用名称！");
        }

        return new FlexThreadPoolServiceImpl(applicationName, threadPoolExecutorMap);
    }

}
