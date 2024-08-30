package com.cong.middleware.flex.pool.sdk.registry;


import com.cong.middleware.flex.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 注册策略
 *
 * @author cong
 * @date 2024/08/30
 */
public interface RegistryStrategy {

    /**
     * 报告线程池（批量）
     *
     * @param threadPoolEntities 线程池实体
     */
    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities);

    /**
     * 报告线程池
     *
     * @param threadPoolConfigEntity 线程池配置实体
     */
    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);

}
