package com.github.yuliyadzemidovich.aviationapi.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Resilience wrapper for sync/async web client calls.
 * Adds retries and circuit breaker
 */
@Service
@RequiredArgsConstructor
public class ResilienceExecutor {

    private final RetryRegistry retryRegistry;
    private final CircuitBreakerRegistry cbRegistry;

    // sync wrapper
    public <T> T execute(String name, Supplier<T> action) {

        // add retry
        Retry retry = retryRegistry.retry(name);
        Supplier<T> withRetry = Retry.decorateSupplier(retry, action);

        // add circuit breaker
        CircuitBreaker cb = cbRegistry.circuitBreaker(name);
        Supplier<T> withRetryAndCb = CircuitBreaker.decorateSupplier(cb, withRetry);

        return withRetryAndCb.get();
    }

    // async wrapper
    public <T> Mono<T> executeMono(String name, Supplier<Mono<T>> action) {
        Retry retry = retryRegistry.retry(name);
        CircuitBreaker cb = cbRegistry.circuitBreaker(name);
        return action.get()
                .transformDeferred(CircuitBreakerOperator.of(cb))
                .transformDeferred(RetryOperator.of(retry));
    }
}
