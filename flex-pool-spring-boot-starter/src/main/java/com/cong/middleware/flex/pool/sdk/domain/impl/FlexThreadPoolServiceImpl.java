package com.cong.middleware.flex.pool.sdk.domain.impl;

import com.alibaba.fastjson.JSON;
import com.cong.middleware.flex.pool.sdk.domain.FlexThreadPoolService;
import com.cong.middleware.flex.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Flex 线程池服务
 *
 * @author cong
 * @date 2024/08/30
 */
public class FlexThreadPoolServiceImpl implements FlexThreadPoolService {

    private final Logger logger = LoggerFactory.getLogger(FlexThreadPoolServiceImpl.class);

    private final String applicationName;
    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;


    public FlexThreadPoolServiceImpl(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    /**
     * 查询线程池列表
     *
     * @return {@link List }<{@link ThreadPoolConfigEntity }>
     */
    @Override
    public List<ThreadPoolConfigEntity> queryThreadPoolList() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntityList = new ArrayList<>(threadPoolExecutorMap.size());

        threadPoolExecutorMap.forEach((k, v) -> {
            ThreadPoolConfigEntity threadPoolConfigEntity = createThreadPoolConfigEntity(k, v);
            threadPoolConfigEntityList.add(threadPoolConfigEntity);
        });
        return threadPoolConfigEntityList;
    }

    /**
     * 按名称查询线程池配置
     *
     * @param threadPoolName 线程池名称
     * @return {@link ThreadPoolConfigEntity }
     */
    @Override
    public ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (ObjectUtils.isEmpty(threadPoolExecutor)) {
            return new ThreadPoolConfigEntity(applicationName, threadPoolName);
        }

        // 线程池配置数据
        ThreadPoolConfigEntity threadPoolConfigEntity = createThreadPoolConfigEntity(threadPoolName, threadPoolExecutor);

        logger.debug("动态线程池，配置查询 应用名:{} 线程名:{} 池化配置:{}", applicationName, threadPoolName, JSON.toJSONString(threadPoolConfigEntity));

        return threadPoolConfigEntity;
    }

    /**
     * 更新线程池配置
     *
     * @param threadPoolConfigEntity 线程池配置实体
     */
    @Override
    public void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity) {
        if (ObjectUtils.isEmpty(threadPoolConfigEntity) || !applicationName.equals(threadPoolConfigEntity.getAppName())) {
            return;
        }
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (ObjectUtils.isEmpty(threadPoolExecutor)) {
            return;
        }

        // 设置参数 「调整核心线程数和最大线程数」
        threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
    }

    private ThreadPoolConfigEntity createThreadPoolConfigEntity(String threadPoolName, ThreadPoolExecutor threadPoolExecutor) {

        ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity(applicationName, threadPoolName);
        threadPoolConfigEntity.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
        threadPoolConfigEntity.setMaximumPoolSize(threadPoolExecutor.getMaximumPoolSize());
        threadPoolConfigEntity.setActiveCount(threadPoolExecutor.getActiveCount());
        threadPoolConfigEntity.setPoolSize(threadPoolExecutor.getPoolSize());
        threadPoolConfigEntity.setQueueType(threadPoolExecutor.getQueue().getClass().getSimpleName());
        threadPoolConfigEntity.setQueueSize(threadPoolExecutor.getQueue().size());
        threadPoolConfigEntity.setRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());

        return threadPoolConfigEntity;
    }
}
