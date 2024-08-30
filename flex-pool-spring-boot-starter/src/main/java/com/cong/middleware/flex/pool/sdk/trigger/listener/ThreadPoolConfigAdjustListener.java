package com.cong.middleware.flex.pool.sdk.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.cong.middleware.flex.pool.sdk.domain.FlexThreadPoolService;
import com.cong.middleware.flex.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.cong.middleware.flex.pool.sdk.registry.RegistryStrategy;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 线程池配置 Adjust 监听器
 *
 * @author cong
 * @date 2024/08/30
 */
public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    private final FlexThreadPoolService flexThreadPoolService;

    private final RegistryStrategy registry;

    public ThreadPoolConfigAdjustListener(FlexThreadPoolService flexThreadPoolService, RegistryStrategy registry) {
        this.flexThreadPoolService = flexThreadPoolService;
        this.registry = registry;
    }

    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("动态线程池，调整线程池配置。线程池名称:{} 核心线程数:{} 最大线程数:{}", threadPoolConfigEntity.getThreadPoolName(), threadPoolConfigEntity.getPoolSize(), threadPoolConfigEntity.getMaximumPoolSize());
        flexThreadPoolService.updateThreadPoolConfig(threadPoolConfigEntity);

        // 更新后上报最新数据
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = flexThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);

        ThreadPoolConfigEntity threadPoolConfigEntityCurrent = flexThreadPoolService.queryThreadPoolConfigByName(threadPoolConfigEntity.getThreadPoolName());
        registry.reportThreadPoolConfigParameter(threadPoolConfigEntityCurrent);
        logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
    }

}
