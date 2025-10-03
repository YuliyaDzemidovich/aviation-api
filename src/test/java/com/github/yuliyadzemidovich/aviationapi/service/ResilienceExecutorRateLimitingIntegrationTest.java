package com.github.yuliyadzemidovich.aviationapi.service;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "resilience4j.retry.instances.aviation.max-attempts=1", // disable retries
                "resilience4j.ratelimiter.instances.aviation.limit-for-period=1",
                "resilience4j.ratelimiter.instances.aviation.limit-refresh-period=5s",
                "resilience4j.ratelimiter.instances.aviation.timeout-duration=0"
        }
)
class ResilienceExecutorRateLimitingIntegrationTest {

    static okhttp3.mockwebserver.MockWebServer server;

    @Autowired
    AirportInfoProvider provider;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) throws Exception {
        server = new okhttp3.mockwebserver.MockWebServer();
        server.start();
        registry.add("aviation.base.url", () -> server.url("/").toString());
    }

    @AfterAll
    static void afterAll() throws IOException {
        server.shutdown();
    }

    @Test
    void breakerOpensThenFastFails() {
        // mock real call
        server.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(200));

        // given first request is ok
        provider.lookup("KATL");

        // when 2nd request happens - then fast fail due to rate limiter
        assertThrows(RequestNotPermitted.class, () -> provider.lookup("KATL"));

        // and assert only 1 real call was made - rate limiter prevented 2d call
        assertEquals(1, server.getRequestCount());
    }
}
