package co.com.crediya.usecases.request.getallrequests;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.PagedResponse;
import co.com.crediya.model.solicitud.valueobjects.SortSpec;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor

public class ListPendingRequestsUseCase {
    private final RequestRepository requestRepository;

    public Mono<PagedResponse<Request>> execute(int page, int size, SortSpec sort) {
        UUID pendingId = StatusName.PENDING_TO_CHECK.getId();
        long offset = (long) page * size;

        Mono<List<Request>> content = requestRepository
                .findPageByStatusId(pendingId, size, offset, sort)
                .collectList();

        Mono<Long> total = requestRepository.countByStatusId(pendingId);

        return Mono.zip(content, total)
                .map(tuple -> PagedResponse.of(tuple.getT1(), page, size, tuple.getT2()));
    }

}
