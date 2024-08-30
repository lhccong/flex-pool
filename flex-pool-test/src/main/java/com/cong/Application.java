package com.cong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Configuration
public class Application {
    public static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public ApplicationRunner applicationRunner(ExecutorService threadPoolExecutor01) {
        return args -> {
            while (true){
                // 创建一个随机时间生成器
                Random random = new Random();
                // 随机时间，用于模拟任务启动延迟
                int initialDelay = random.nextInt(10) + 1; // 1到10秒之间
                // 随机休眠时间，用于模拟任务执行时间
                int sleepTime = random.nextInt(10) + 1; // 1到10秒之间

                // 提交任务到线程池
                threadPoolExecutor01.submit(() -> {
                    try {
                        // 模拟任务启动延迟
                        TimeUnit.SECONDS.sleep(initialDelay);
                        logger.info("Task started after {} seconds.", initialDelay);

                        // 模拟任务执行
                        TimeUnit.SECONDS.sleep(sleepTime);
                        logger.info("Task executed for {} seconds.", sleepTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

                Thread.sleep(random.nextInt(50) + 1);
            }
        };
    }
}
