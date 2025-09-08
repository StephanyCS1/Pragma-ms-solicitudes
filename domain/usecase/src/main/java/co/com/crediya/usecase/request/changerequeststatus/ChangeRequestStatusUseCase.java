package co.com.crediya.usecase.request.changerequeststatus;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ChangeRequestStatusUseCase {

    private RequestRepository requestRepository;

    public Mono<Request> update(UUID requestId, String newStatusName) {
        Optional<StatusName> statusName = StatusName.fromName(newStatusName);
        if (statusName.isEmpty()) {
            return Mono.error(new DomainValidationException("Estado no válido: " + newStatusName));
        }

        return requestRepository.findRequestById(requestId)
                .switchIfEmpty(Mono.error(new DomainValidationException("Solicitud no encontrada")))
                .flatMap(request -> validateStatusChange(request, statusName.get().getId()))
                .flatMap(request -> requestRepository.updateStatus(requestId, statusName.get().getId()));
    }

    private Mono<Request> validateStatusChange(Request request, UUID newStatusId) {
        if (request.getStatusId().equals(newStatusId)) {
            return Mono.error(new DomainValidationException("El estado debe ser diferente al actual"));
        }
        return Mono.just(request);
    }
}
