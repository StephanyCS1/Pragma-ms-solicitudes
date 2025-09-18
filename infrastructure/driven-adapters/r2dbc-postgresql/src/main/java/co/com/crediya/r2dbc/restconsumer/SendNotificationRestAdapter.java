package co.com.crediya.r2dbc.restconsumer;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.SendNotification;
import co.com.crediya.model.solicitud.valueobjects.SendNotificationData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SendNotificationRestAdapter implements SendNotification {

    private final WebClient notificationServiceClient;

    public SendNotificationRestAdapter(@Qualifier("notificationServiceClient") WebClient notificationServiceClient) {
        this.notificationServiceClient = notificationServiceClient;
    }

    @Value("${external.notification.base-path:/api/v1/aws}")
    private String notificationPath;

    @Override
    public Mono<Boolean> sendNotification(SendNotificationData notification, String token) {
        return notificationServiceClient
                .post()
                .uri(notificationPath + "/send-notification")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(notification)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.is2xxSuccessful()) {
                        return Mono.just(true);
                    }

                    if (status.value() == 404) {
                        return Mono.error(new DomainValidationException("Servicio de notificaciones no encontrado"));
                    }

                    if (status.is4xxClientError()) {
                        return response.bodyToMono(String.class)
                                .defaultIfEmpty("Error en la petición de notificación")
                                .flatMap(msg -> Mono.error(new DomainValidationException("Error 4xx: " + msg)));
                    }

                    return response.bodyToMono(String.class)
                            .defaultIfEmpty("Error del servidor de notificaciones")
                            .flatMap(msg -> Mono.error(new DomainValidationException("Error 5xx: " + msg)));
                });
    }
}