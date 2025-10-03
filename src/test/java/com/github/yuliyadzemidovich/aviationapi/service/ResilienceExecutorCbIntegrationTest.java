package com.github.yuliyadzemidovich.aviationapi.service;

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
                "resilience4j.retry.instances.aviation.max-attempts=1", // disable retries, since we're testing CB only
                "resilience4j.circuitbreaker.instances.aviation.sliding-window-size=4",
                "resilience4j.circuitbreaker.instances.aviation.minimum-number-of-calls=4",
                "resilience4j.circuitbreaker.instances.aviation.failure-rate-threshold=50",
                "resilience4j.circuitbreaker.instances.aviation.wait-duration-in-open-state=10s"
        }
)
class ResilienceExecutorCbIntegrationTest {

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
        // mock 4 failed requests
        server.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(500));
        server.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(502));
        server.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(503));
        server.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(504));

        // given 4 requests fail
        for (int i = 0; i < 4; i++) {
            assertThrows(RuntimeException.class, () -> provider.lookup("KATL"));
        }
        assertEquals(4, server.getRequestCount());

        // then fast fail on 5th attempt
        assertThrows(RuntimeException.class, () -> provider.lookup("KATL"));
        // assert no 5th real call attempt (circuit breaker works as expected)
        assertEquals(4, server.getRequestCount());
    }
}
