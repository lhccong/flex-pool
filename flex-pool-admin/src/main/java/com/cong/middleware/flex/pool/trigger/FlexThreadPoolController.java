package com.cong.middleware.flex.pool.trigger;

import com.alibaba.fastjson.JSON;
import com.cong.middleware.flex.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.cong.middleware.flex.pool.types.Response;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/flex/thread/pool/")
public class FlexThreadPoolController {

    @Resource
    public RedissonClient redissonClient;

    /**
     * 查询线程池数据
     * curl --request GET  --url http://localhost:8089/api/v1/flex/thread/pool/query_thread_pool_list
     */
    @GetMapping("query_thread_pool_list")
    public Response<List<ThreadPoolConfigEntity>> queryThreadPoolList() {
        try {
            RList<ThreadPoolConfigEntity> cacheList = redissonClient.getList("THREAD_POOL_CONFIG_LIST_KEY");
            return Response.<List<ThreadPoolConfigEntity>>builder()
                    .code(Response.Code.SUCCESS.getCode())
                    .info(Response.Code.SUCCESS.getInfo())
                    .data(cacheList.readAll())
                    .build();
        } catch (Exception e) {
            log.error("查询线程池数据异常", e);
            return Response.<List<ThreadPoolConfigEntity>>builder()
                    .code(Response.Code.UN_ERROR.getCode())
                    .info(Response.Code.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 查询线程池配置
     * curl --request GET \
     * --url '<a href="http://localhost:8089/api/v1/flex/thread/pool/query_thread_pool_config?appName=flex-thread-pool-test-app&threadPoolName=threadPoolExecutor">...</a>'
     */
    @GetMapping("query_thread_pool_config")
    public Response<ThreadPoolConfigEntity> queryThreadPoolConfig(@RequestParam String appName, @RequestParam String threadPoolName) {
        try {
            String cacheKey = "THREAD_POOL_CONFIG_PARAMETER_LIST_KEY" + "_" + appName + "_" + threadPoolName;
            ThreadPoolConfigEntity threadPoolConfigEntity = redissonClient.<ThreadPoolConfigEntity>getBucket(cacheKey).get();
            return Response.<ThreadPoolConfigEntity>builder()
                    .code(Response.Code.SUCCESS.getCode())
                    .info(Response.Code.SUCCESS.getInfo())
                    .data(threadPoolConfigEntity)
                    .build();
        } catch (Exception e) {
            log.error("查询线程池配置异常", e);
            return Response.<ThreadPoolConfigEntity>builder()
                    .code(Response.Code.UN_ERROR.getCode())
                    .info(Response.Code.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * 修改线程池配置
     * curl --request POST --url http://localhost:8089/api/v1/flex/thread/pool/update_thread_pool_config
     * --header 'content-type: application/json' \
     * --data '{
     * "appName":"flex-thread-pool-test-app",
     * "threadPoolName": "threadPoolExecutor",
     * "corePoolSize": 1,
     * "maximumPoolSize": 10
     * }'
     */
    @PostMapping("update_thread_pool_config")
    public Response<Boolean> updateThreadPoolConfig(@RequestBody ThreadPoolConfigEntity request) {
        try {
            log.info("修改线程池配置开始 {} {} {}", request.getAppName(), request.getThreadPoolName(), JSON.toJSONString(request));
            RTopic topic = redissonClient.getTopic("FLEX_THREAD_POOL_REDIS_TOPIC" + "_" + request.getAppName());
            topic.publish(request);
            log.info("修改线程池配置完成 {} {}", request.getAppName(), request.getThreadPoolName());
            return Response.<Boolean>builder()
                    .code(Response.Code.SUCCESS.getCode())
                    .info(Response.Code.SUCCESS.getInfo())
                    .data(true)
                    .build();
        } catch (Exception e) {
            log.error("修改线程池配置异常 {}", JSON.toJSONString(request), e);
            return Response.<Boolean>builder()
                    .code(Response.Code.UN_ERROR.getCode())
                    .info(Response.Code.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

}
