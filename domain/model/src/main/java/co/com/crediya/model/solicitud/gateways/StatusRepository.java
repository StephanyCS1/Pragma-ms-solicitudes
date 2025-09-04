package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Status;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StatusRepository {

    Mono<Status> findStatusById(UUID id);
    Mono<Status> findIdByName(String name);
    Flux<Status> findAllStatuses();
}
