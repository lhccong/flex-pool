package com.cong.middleware.flex.pool;

import com.cong.middleware.flex.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RTopic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * API 测试
 *
 * @author cong
 * @date 2024/08/30
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {
    @Resource
    private RTopic flexThreadPoolRedisTopic;

    @Test
    public void test_dynamicThreadPoolRedisTopic() throws InterruptedException {
        ThreadPoolConfigEntity threadPoolConfigEntity = new ThreadPoolConfigEntity("flex-pool-test-app", "threadPoolExecutor01");
        threadPoolConfigEntity.setPoolSize(100);
        threadPoolConfigEntity.setMaximumPoolSize(100);
        flexThreadPoolRedisTopic.publish(threadPoolConfigEntity);

        new CountDownLatch(1).await();
    }
}
