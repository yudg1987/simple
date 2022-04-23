package com.dgyu.pure_design.common.config.executor;

import com.dgyu.pure_design.common.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author yychen3
 * @date 2019-06-20
 */
@Configuration
public class ExecutorConfig {

    @Autowired
    private ApplicationConfig applicationConfig;

    private ThreadPoolTaskExecutor initExecutor(String threadPoolName) {
        int corePoolSize = applicationConfig.getCorePoolSize();
        int maxPoolSize = applicationConfig.getMaxPoolSize();
        int queueCapacity = applicationConfig.getQueueSize();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(0);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix(threadPoolName);
        executor.initialize();
        return executor;
    }


    @Bean("aopLogExecutor")
    public ThreadPoolTaskExecutor aopLogExecutor() {
        return this.initExecutor("aopLog-pool-");
    }

}
