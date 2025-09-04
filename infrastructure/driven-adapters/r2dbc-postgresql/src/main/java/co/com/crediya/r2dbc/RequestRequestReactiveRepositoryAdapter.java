package co.com.crediya.r2dbc;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.*;
import co.com.crediya.r2dbc.entity.RequestEntity;
import co.com.crediya.r2dbc.entity.StatusEntity;
import co.com.crediya.r2dbc.helper.RequestReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public class RequestRequestReactiveRepositoryAdapter extends RequestReactiveAdapterOperations<
        Request,
        RequestEntity,
    UUID,
        RequestReactiveRepository
> implements RequestRepository {

    public RequestRequestReactiveRepositoryAdapter(RequestReactiveRepository repository, ObjectMapper mapper) {

        super(repository, mapper, d -> mapper.map(d, Request.class));
    }

    @Override
    @Transactional()
    public Mono<Request> save(Request request) {
        RequestEntity requestEntity = toEntity(request);
        return repository.save(requestEntity).map(this::toDomain);
    }

    @Override
    @Transactional()
    public Mono<Request> updateStatus(UUID id, UUID status) {
        return Mono.just(id)
                .flatMap(this::findRequestById)
                .map(this::toEntity)
                .flatMap(repository::save)
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Request> findRequestById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Request> findRequestsByEmail(Email email) {
        return repository.findByEmail(email.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Request> findAllRequests() {
        return repository.findAll().map(this::toDomain);
    }

    @Override
    @Transactional()
    public Mono<Void> deleteRequest(UUID id, String email) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La solicitud "+ id+ " no existe")))
                .flatMap(exist -> {
                    return repository.deleteById(id);
                });
    }


    public RequestEntity toEntity(Request request) {
        return RequestEntity.builder()
                .id(request.getId() != null ? request.getId() : null)
                .documentNumber(request.getDocumentNumber().document())
                .email(request.getEmail().value())
                .requestedAmount(new BigDecimal(String.valueOf(request.getRequestedAmount().amount())))
                .loanTerm(request.getLoanTerm().asString())
                .loanTypeId(request.getLoanTypeId())
                .statusId(request.getStatusId())
                .requestDate(request.getRequestDate())
                .lastUpdateDate(request.getLastUpdateDate())
                .build();
    }

    public Request toDomain(RequestEntity entity) {
        return Request.builder()
                .id(entity.getId())
                .documentNumber(new Identification(entity.getDocumentNumber()))
                .email(new Email(entity.getEmail()))
                .requestedAmount(new Amount(entity.getRequestedAmount()))
                .loanTerm(new LoanTerm(Integer.parseInt(entity.getLoanTerm())))
                .loanTypeId(entity.getLoanTypeId())
                .statusId(entity.getStatusId())
                .requestDate(entity.getRequestDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .build();
    }
}
