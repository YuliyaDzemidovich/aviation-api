package com.github.yuliyadzemidovich.aviationapi.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.yuliyadzemidovich.aviationapi.service.AirportInfoProvider;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class AirportInfoProviderAviationApiImpl implements AirportInfoProvider {

    private final WebClient aviationWebClient;

    @Override
    public AirportInfo lookup(String code) {
        long t1 = System.currentTimeMillis();

        JsonNode response = aviationWebClient.get()
                .uri(uri -> uri.queryParam("apt", code).build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        long t2 = System.currentTimeMillis();
        log.info("lookup for code {} - lookup took {} ms", code, t2 - t1);

        return new AirportInfo(response);
    }
}
