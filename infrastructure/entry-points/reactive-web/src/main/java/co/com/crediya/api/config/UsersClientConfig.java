package co.com.crediya.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UsersClientConfig {

    @Value("${external.users.base-url:http://localhost:8080}")
    private String usersBaseUrl;

    @Bean
    public WebClient userServiceClient(WebClient.Builder builder) {
        return builder
                .baseUrl(usersBaseUrl)
                .filter((request, next) ->
                        ReactiveSecurityContextHolder.getContext()
                                .map(ctx -> ctx.getAuthentication())
                                .map(auth -> String.valueOf(auth.getCredentials()))
                                .filter(token -> token != null && !token.isBlank())
                                .map(token -> ClientRequest.from(request)
                                        .headers(h -> h.set(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                        .build())
                                .defaultIfEmpty(request)
                                .flatMap(next::exchange)
                )
                .build();
    }
}