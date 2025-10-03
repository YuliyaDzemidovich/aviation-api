package com.github.yuliyadzemidovich.aviationapi.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.yuliyadzemidovich.aviationapi.exception.UpstreamUnavailableException;
import com.github.yuliyadzemidovich.aviationapi.service.AirportInfoProvider;
import com.github.yuliyadzemidovich.aviationapi.service.ResilienceExecutor;
import com.github.yuliyadzemidovich.aviationapi.service.dto.AirportInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AirportInfoProviderAviationApiImpl implements AirportInfoProvider {

    private final WebClient aviationWebClient;
    private final ResilienceExecutor executor;

    @Override
    public AirportInfo lookup(String code) {
        long t1 = System.currentTimeMillis();

        JsonNode response = null;
        try {
            response = executor.execute("aviation", () -> callUpstream(code).block());
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                throw new UpstreamUnavailableException("Upstream unavailable after retries", e);
            }
        }

        long t2 = System.currentTimeMillis();
        log.info("lookup for code {} - lookup took {} ms", code, t2 - t1);

        return new AirportInfo(response);
    }

    @NotNull
    private Mono<JsonNode> callUpstream(String code) {
        return aviationWebClient.get()
                .uri(uri -> uri.queryParam("apt", code).build())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}
