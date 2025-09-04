package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.valueobjects.Email;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RequestRepository {

    Mono<Request> save(Request request);
    Mono<Request> updateStatus(UUID id, UUID status);
    Mono<Request> findRequestById(UUID id);
    Flux<Request> findRequestsByEmail(Email email);
    Flux<Request> findAllRequests();
    Mono<Void> deleteRequest(UUID id, String email);

}