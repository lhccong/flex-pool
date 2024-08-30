package com.cong.middleware.flex.pool.sdk.config;

import com.cong.middleware.flex.pool.sdk.domain.FlexThreadPoolService;
import com.cong.middleware.flex.pool.sdk.domain.impl.FlexThreadPoolServiceImpl;
import com.cong.middleware.flex.pool.sdk.registry.RegistryStrategy;
import com.cong.middleware.flex.pool.sdk.registry.redis.RedisRegistry;
import com.cong.middleware.flex.pool.sdk.trigger.job.ThreadPoolDataReportJob;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Flex Pool 自动配置 入口
 *
 * @author cong
 * @date 2024/08/30
 */
@EnableConfigurationProperties(FlexPoolAutoProperties.class)
@EnableScheduling
@Configuration
public class FlexThreadPoolAutoConfig {
    private final Logger logger = LoggerFactory.getLogger(FlexThreadPoolAutoConfig.class);

    private String applicationName;

    @Bean("flexPoolRedissonClient")
    public RedissonClient redissonClient(FlexPoolAutoProperties properties) {
        Config config = new Config();
        // 根据需要可以设定编解码器；https://github.com/redisson/redisson/wiki/4.-%E6%95%B0%E6%8D%AE%E5%BA%8F%E5%88%97%E5%8C%96
        config.setCodec(JsonJacksonCodec.INSTANCE);

        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize())
                .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                .setIdleConnectionTimeout(properties.getIdleTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setPingConnectionInterval(properties.getPingInterval())
                .setKeepAlive(properties.isKeepAlive())
        ;

        RedissonClient redissonClient = Redisson.create(config);

        logger.info("动态线程池，注册器（redis）链接初始化完成。{} {} {}", properties.getHost(), properties.getPoolSize(), !redissonClient.isShutdown());

        return redissonClient;
    }

    @Bean
    public RegistryStrategy redisRegistry(RedissonClient flexPoolRedissonClient) {
        return new RedisRegistry(flexPoolRedissonClient);
    }

    @Bean("flexThreadPollService")
    public FlexThreadPoolService flexThreadPollService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");

        if (StringUtils.isBlank(applicationName)) {
            applicationName = "无法找到应用名称";
            logger.warn("动态线程池，启动提示。SpringBoot 应用未配置 spring.application.name 无法获取到应用名称！");
        }

        return new FlexThreadPoolServiceImpl(applicationName, threadPoolExecutorMap);
    }
    @Bean
    public ThreadPoolDataReportJob threadPoolDataReportJob(FlexThreadPoolService flexThreadPoolService, RegistryStrategy registryStrategy) {
        return new ThreadPoolDataReportJob(flexThreadPoolService, registryStrategy);
    }

}
