package co.com.crediya.r2dbc.restconsumer;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.model.solicitud.valueobjects.GeneralResponse;
import co.com.crediya.model.solicitud.valueobjects.pojo.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserValidationRestAdapter implements UserValidationService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<UserResponse> findByEmail(String email) {
        return webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8080)
                        .path("/api/v1/usuarios")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND),
                        response -> Mono.error(new DomainValidationException("Usuario no encontrado")))
                .bodyToMono(new ParameterizedTypeReference<GeneralResponse<UserResponse>>() {})
                .map(GeneralResponse::data)
                .onErrorResume(WebClientException.class,
                        ex -> Mono.error(new DomainValidationException("Error al consultar usuario")));
    }


}