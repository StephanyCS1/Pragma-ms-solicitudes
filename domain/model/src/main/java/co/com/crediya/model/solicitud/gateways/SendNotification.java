package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.valueobjects.SendNotificationData;
import reactor.core.publisher.Mono;

public interface SendNotification {
    Mono<Boolean> sendNotification(SendNotificationData notification, String token);
}
