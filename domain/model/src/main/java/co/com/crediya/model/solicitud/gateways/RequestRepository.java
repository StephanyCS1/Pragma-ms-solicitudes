package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.valueobjects.Email;
import co.com.crediya.model.solicitud.valueobjects.SortSpec;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface RequestRepository {

    Mono<Request> save(Request request);
    Mono<Request> updateStatus(UUID id, UUID status);
    Mono<Request> findRequestById(UUID id);
    Flux<Request> findRequestsByEmail(Email email);
    Flux<Request> findAllRequests();
    Mono<Void> deleteRequest(UUID id, String email);


    Flux<Request>findPageByStatusesWithFilter(List<UUID> statuses, long limit, long offset, SortSpec sort, String filterType, String filterValue);

    Mono<Long> countByStatusesWithFilter(List<UUID> statuses, String filterType, String filterValue);
}