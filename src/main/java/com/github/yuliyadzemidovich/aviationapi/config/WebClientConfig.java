package com.github.yuliyadzemidovich.aviationapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient aviationWebClient(@Value("${aviation.base.url}") String base) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(20));
        return WebClient.builder()
                .baseUrl(base)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
