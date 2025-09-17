package co.com.crediya.usecase.request.deleterequest;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.exceptions.RequestNotFoundException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.Email;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class DeleteRequestUseCase {

    private final RequestRepository requestRepository;

    public Mono<Void> excecute(String id, String email) {
        Email owner = new Email(email);
        UUID uuid = UUID.fromString(id);
        return requestRepository.findRequestById(uuid)
                .switchIfEmpty(Mono.error(new RequestNotFoundException("Solicitud no encontrada")))
                .flatMap(exist -> validateOwnership(exist, owner))
                .flatMap(request -> requestRepository.deleteRequest(uuid, owner.value()))
                .then();
    }

    private Mono<Request> validateOwnership(Request request, Email ownerEmail) {
        if (!request.getEmail().equals(ownerEmail)) {
            return Mono.error(new DomainValidationException("Solo el propietario puede eliminar la solicitud"));
        }
        return Mono.just(request);
    }
}
