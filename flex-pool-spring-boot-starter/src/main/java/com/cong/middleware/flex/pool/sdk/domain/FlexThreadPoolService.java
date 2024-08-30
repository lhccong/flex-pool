package com.cong.middleware.flex.pool.sdk.domain;

import com.cong.middleware.flex.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * Flex 线程池服务
 *
 * @author cong
 * @date 2024/08/30
 */
public interface FlexThreadPoolService {

    /**
     * 查询线程池列表
     *
     * @return {@link List }<{@link ThreadPoolConfigEntity }>
     */
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    /**
     * 按名称查询线程池配置
     *
     * @param threadPoolName 线程池名称
     * @return {@link ThreadPoolConfigEntity }
     */
    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    /**
     * 更新线程池配置
     *
     * @param threadPoolConfigEntity 线程池配置实体
     */
    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);
}
