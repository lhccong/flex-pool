package com.cong.middleware.flex.pool.sdk.registry.redis;

import com.cong.middleware.flex.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.cong.middleware.flex.pool.sdk.domain.model.enums.RegistryEnum;
import com.cong.middleware.flex.pool.sdk.registry.RegistryStrategy;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.List;


/**
 * Redis 注册表
 *
 * @author cong
 * @date 2024/08/30
 */
public class RedisRegistry implements RegistryStrategy {

    private final RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 报告线程池（批量）
     *
     * @param threadPoolEntities 线程池实体
     */
    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities) {
        RList<ThreadPoolConfigEntity> list = redissonClient.getList(RegistryEnum.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        threadPoolEntities.forEach(item -> {
            if (!list.contains(item)){
                list.add(item);
            }
        });
    }

    /**
     * 报告线程池
     *
     * @param threadPoolConfigEntity 线程池配置实体
     */
    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnum.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" + threadPoolConfigEntity.getAppName() + "_" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }
}
