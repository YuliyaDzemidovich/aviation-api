package com.github.yuliyadzemidovich.aviationapi.config;

import com.github.yuliyadzemidovich.aviationapi.service.ResilienceExecutor;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

    @Bean
    public ResilienceExecutor resilienceExecutor(RetryRegistry retryRegistry,
                                                 CircuitBreakerRegistry cbRegistry,
                                                 RateLimiterRegistry rlRegistry) {
        return new ResilienceExecutor(retryRegistry, cbRegistry, rlRegistry);
    }
}
