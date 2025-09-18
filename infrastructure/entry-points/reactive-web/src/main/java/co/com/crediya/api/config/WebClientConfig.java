package co.com.crediya.api.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@Slf4j
public class WebClientConfig {

    @Bean
    @Qualifier("userServiceClient")
    public WebClient userServiceClient(
            @Value("${external.users.base-url}") String baseUrl,
            WebClient.Builder webClientBuilder) {

        return webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(1 * 1024 * 1024))
                .build();
    }

    @Bean
    @Qualifier("notificationServiceClient")
    public WebClient notificationServiceClient(
            @Value("${external.notification.base-url}") String baseUrl,
            WebClient.Builder webClientBuilder) {

        return webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(1 * 1024 * 1024))
                .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                                .responseTimeout(Duration.ofSeconds(30))
                                .doOnConnected(conn ->
                                        conn.addHandlerLast(new ReadTimeoutHandler(30))
                                                .addHandlerLast(new WriteTimeoutHandler(30)))
                ))
                .filter(logRequest())
                .filter(logResponse())
                .defaultStatusHandler(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(String.class)
                            .defaultIfEmpty("Error desconocido")
                            .flatMap(errorBody -> {
                                log.error("Error HTTP {}: {}", response.statusCode(), errorBody);
                                return Mono.error(new RuntimeException(
                                        String.format("HTTP %s: %s", response.statusCode(), errorBody)));
                            });
                });
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                clientRequest.headers().forEach((name, values) ->
                        values.forEach(value -> log.debug("{}={}", name, value)));
            }
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (log.isDebugEnabled()) {
                log.debug("Response Status: {}", clientResponse.statusCode());
            }
            return Mono.just(clientResponse);
        });
    }
}