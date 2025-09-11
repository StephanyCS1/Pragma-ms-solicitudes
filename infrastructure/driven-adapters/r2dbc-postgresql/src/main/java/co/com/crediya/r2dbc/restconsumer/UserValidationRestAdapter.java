package co.com.crediya.r2dbc.restconsumer;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.model.solicitud.valueobjects.GeneralResponse;
import co.com.crediya.model.solicitud.valueobjects.pojo.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserValidationRestAdapter implements UserValidationService {

    private final WebClient userServiceClient;
    @Value("${external.users.base-path:/api/v1/usuarios}")
    private String usersPath;

    @Override
    public Mono<UserResponse> findByEmail(String email, String token) {
        return userServiceClient.get()
                .uri(uri -> uri.path(usersPath).queryParam("email", email).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchangeToMono(resp -> {
                    var status = resp.statusCode();
                    if (status.is2xxSuccessful()) {
                        return resp.bodyToMono(new ParameterizedTypeReference<GeneralResponse<UserResponse>>() {})
                                .map(GeneralResponse::data);
                    }
                    if (status.value() == 404) {
                        return Mono.error(new DomainValidationException("Usuario no encontrado"));
                    }
                    if (status.is4xxClientError()) {
                        return resp.bodyToMono(String.class)
                                .defaultIfEmpty("Solicitud inválida al servicio de usuarios")
                                .flatMap(msg -> Mono.error(new DomainValidationException(msg)));
                    }
                    return resp.bodyToMono(String.class)
                            .defaultIfEmpty("Error al consultar usuario")
                            .flatMap(msg -> Mono.error(new DomainValidationException(msg)));
                });
    }
}