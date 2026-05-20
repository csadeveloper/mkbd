package com.ciptadana.mkbd_master_menu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    public static final String MKBD_QUERY_EXECUTOR = "mkbdQueryExecutor";

    @Bean(name = MKBD_QUERY_EXECUTOR)
    public Executor mkbdQueryExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(6);
        executor.setMaxPoolSize(6);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("mkbd-query-");
        executor.initialize();
        return executor;
    }
}
