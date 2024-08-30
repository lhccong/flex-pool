package com.cong.middleware.flex.pool.sdk.trigger.job;

import com.alibaba.fastjson.JSON;
import com.cong.middleware.flex.pool.sdk.domain.FlexThreadPoolService;
import com.cong.middleware.flex.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.cong.middleware.flex.pool.sdk.registry.RegistryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;


/**
 * 线程池数据上报任务
 *
 * @author cong
 * @date 2024/08/30
 */
public class ThreadPoolDataReportJob {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolDataReportJob.class);

    private final FlexThreadPoolService flexThreadPoolService;

    private final RegistryStrategy registry;

    public ThreadPoolDataReportJob(FlexThreadPoolService flexThreadPoolService, RegistryStrategy registry) {
        this.flexThreadPoolService = flexThreadPoolService;
        this.registry = registry;
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void execReportThreadPoolList() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = flexThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);
        logger.info("动态线程池，上报线程池信息：{}", JSON.toJSONString(threadPoolConfigEntities));

        for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities) {
            registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);
            logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
        }

    }

}
