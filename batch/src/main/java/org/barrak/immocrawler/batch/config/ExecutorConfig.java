package org.barrak.immocrawler.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean
    public Executor getExecutor() {
        return Executors.newFixedThreadPool(20) ;
    }
}
