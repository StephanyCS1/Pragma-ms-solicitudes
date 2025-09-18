package co.com.crediya.usecase.request.changerequeststatus;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.SendNotification;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.model.solicitud.valueobjects.SendNotificationData;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
public class ChangeRequestStatusUseCase {

    private final RequestRepository requestRepository;
    private final SendNotification sendNotification;
    private final UserValidationService userValidationService;

    public Mono<Request> update(UUID requestId, String newStatusName, String token) {
        Optional<StatusName> statusName = StatusName.fromName(newStatusName);
        if (statusName.isEmpty()) {
            return Mono.error(new DomainValidationException("Estado no válido: " + newStatusName));
        }

        return requestRepository.findRequestById(requestId)
                .switchIfEmpty(Mono.error(new DomainValidationException("Solicitud no encontrada")))
                .flatMap(request -> validateStatusChange(request, statusName.get().getId()))
                .flatMap(request -> requestRepository.updateStatus(requestId, statusName.get().getId()))
                .flatMap(updatedRequest -> {
                    if (shouldSendEmail(newStatusName)) {
                        return sendNotificationForStatusChange(updatedRequest, newStatusName, token);
                    } else {
                        return Mono.just(updatedRequest);
                    }
                });
    }

    private boolean shouldSendEmail(String statusName) {
        return "APPROVED".equalsIgnoreCase(statusName) || "REJECTED".equalsIgnoreCase(statusName);
    }

    private Mono<Request> sendNotificationForStatusChange(Request request, String newStatusName, String token) {
        return userValidationService.findByEmail(request.getEmail().value(), token)
                .switchIfEmpty(Mono.error(new DomainValidationException("Usuario no encontrado")))
                .map(userResponse -> userResponse.name() != null ? userResponse.name() : "Usuario")
                .flatMap(userName -> {
                    SendNotificationData data = normalizeData(request, newStatusName, userName);
                    return sendNotification.sendNotification(data, token)
                            .then(Mono.just(request));
                })
                .onErrorReturn(request);
    }

    private Mono<Request> validateStatusChange(Request request, UUID newStatusId) {
        if (request.getStatusId().equals(newStatusId)) {
            return Mono.error(new DomainValidationException("El estado debe ser diferente al actual"));
        }
        return Mono.just(request);
    }

    private SendNotificationData normalizeData(Request request, String newStatusName, String userName) {
        Optional<StatusName> currentStatusName = StatusName.fromId(request.getStatusId());
        String currentStatusNameStr = currentStatusName.map(StatusName::name).orElse("Estado desconocido");

        return new SendNotificationData(
                String.valueOf(request.getId()),
                currentStatusNameStr,
                newStatusName,
                request.getEmail().value(),
                userName
        );
    }
}