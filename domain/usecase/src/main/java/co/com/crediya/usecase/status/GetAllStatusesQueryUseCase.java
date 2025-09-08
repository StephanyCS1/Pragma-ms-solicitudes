package co.com.crediya.usecase.status;

import co.com.crediya.model.solicitud.Status;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.StatusRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class GetAllStatusesQueryUseCase {

    private final StatusRepository repository;

    public Flux<Status> getAll() {
        return repository.findAllStatuses();
    }

    public Mono<Status> getById(UUID id) {
        return repository.findStatusById(id)
                .switchIfEmpty(Mono.error(new DomainValidationException("Estado no encontrado")));
    }

    public Mono<Status> getByName(String name) {
        return repository.findIdByName(name)
                .switchIfEmpty(Mono.error(new DomainValidationException("Estado no encontrado")));
    }
}
