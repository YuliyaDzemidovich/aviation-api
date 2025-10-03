package com.github.yuliyadzemidovich.aviationapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ResilienceExecutorRetryIntegrationTest {

    static okhttp3.mockwebserver.MockWebServer server;

    @Autowired
    AirportInfoProvider provider;

    @DynamicPropertySource
    static void init(DynamicPropertyRegistry registry) throws IOException {
        server = new okhttp3.mockwebserver.MockWebServer();
        server.start();

        // our web client should call the mock server
        registry.add("aviation.base.url", () -> server.url("/").toString());
    }

    @AfterAll
    static void afterAll() throws IOException {
        server.shutdown();
    }

    @Test
    void retriesTwiceThenSuccess() {
        // mock server responses
        server.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(500));
        server.enqueue(new okhttp3.mockwebserver.MockResponse().setResponseCode(500));
        server.enqueue(new okhttp3.mockwebserver.MockResponse()
                .setHeader("Content-Type","application/json")
                .setBody("{\"KORD\":[{\"facility_name\":\"O'HARE\",\"icao_ident\":\"KORD\"}]}"));

        // method under test
        AirportInfo airportInfo = provider.lookup("KORD");

        // verify response
        assertNotNull(airportInfo);
        JsonNode node = airportInfo.getInfo();
        assertEquals("KORD", node.path("KORD").get(0).path("icao_ident").asText());

        // verify retry logic
        assertEquals(3, server.getRequestCount());
    }
}
