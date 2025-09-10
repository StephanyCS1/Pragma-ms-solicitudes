package co.com.crediya.r2dbc;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.exceptions.RequestNotFoundException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.*;
import co.com.crediya.r2dbc.entity.RequestEntity;
import co.com.crediya.r2dbc.helper.RequestReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La solicitud " + id + " no existe")))
                .map(entity -> {
                    entity.setStatusId(status);
                    entity.setLastUpdateDate(OffsetDateTime.now().toLocalDateTime());
                    return entity;
                })
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
    @Transactional
    public Mono<Void> deleteRequest(UUID id, String email) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RequestNotFoundException("La solicitud " + id + " no existe")))
                .then(repository.deleteById(id));
    }

    @Override
    public Flux<Request> findPageByStatusesWithFilter(
            List<UUID> statuses, long limit, long offset,
            SortSpec sort, String filterType, String filterValue) {

        if (limit <= 0) {
            return Flux.error(new IllegalArgumentException("limit must be > 0"));
        }
        int size = Math.toIntExact(limit);
        int page = (int) (offset / limit);

        Sort springSort = toSpringSort(sort == null
                ? new SortSpec("requestDate", SortSpec.Direction.DESC)
                : sort);

        Pageable pb = PageRequest.of(page, size, springSort);

        Flux<RequestEntity> flux;
        if (filterType == null || filterValue == null || filterValue.isBlank()) {
            flux = repository.findByStatusIdIn(statuses, pb);
        } else {
            switch (filterType.toLowerCase()) {
                case "email" -> flux = repository.findByStatusIdInAndEmailContainingIgnoreCase(
                        statuses, filterValue.trim(), pb);
                case "name" -> flux = repository.findByStatusIdInAndNameContainingIgnoreCase(
                        statuses, filterValue.trim(), pb);
                case "amount_min" -> {
                    BigDecimal min = new BigDecimal(filterValue.trim());
                    flux = repository.findByStatusIdInAndRequestedAmountGreaterThanEqual(statuses, min, pb);
                }
                case "amount_max" -> {
                    BigDecimal max = new BigDecimal(filterValue.trim());
                    flux = repository.findByStatusIdInAndRequestedAmountLessThanEqual(statuses, max, pb);
                }
                default -> flux = repository.findByStatusId(StatusName.fromName("PENDING_TO_CHECK").get().getId(), pb);
            }
        }

        return flux.map(this::toDomain);
    }

    @Override
    public Mono<Long> countByStatusesWithFilter(
            List<UUID> statuses, String filterType, String filterValue) {

        if (filterType == null || filterValue == null || filterValue.isBlank()) {
            return repository.countByStatusIdIn(statuses);
        }

        return switch (filterType.toLowerCase()) {
            case "email" -> repository.countByStatusIdInAndEmailContainingIgnoreCase(statuses, filterValue.trim());
            case "name"  -> repository.countByStatusIdInAndNameContainingIgnoreCase(statuses, filterValue.trim());
            case "amount_min" -> {
                BigDecimal min = new BigDecimal(filterValue.trim());
                yield repository.countByStatusIdInAndRequestedAmountGreaterThanEqual(statuses, min);
            }
            case "amount_max" -> {
                BigDecimal max = new BigDecimal(filterValue.trim());
                yield repository.countByStatusIdInAndRequestedAmountLessThanEqual(statuses, max);
            }
            default -> repository.countByStatusIdIn(statuses);
        };
    }



    private Sort toSpringSort(SortSpec sortSpec) {
        if (sortSpec == null || sortSpec.property() == null || sortSpec.property().isBlank()) {
            return Sort.unsorted();
        }
        Sort.Direction dir = (sortSpec.direction() == SortSpec.Direction.DESC)
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, sortSpec.property());
    }

    public RequestEntity toEntity(Request request) {
        return RequestEntity.builder()
                .id(request.getId() != null ? request.getId() : null)
                .name(request.getName().fullName())
                .document(request.getDocumentNumber().document())
                .email(request.getEmail().value())
                .requestedAmount(new BigDecimal(String.valueOf(request.getRequestedAmount().amount())))
                .loanTerm(request.getLoanTerm().months())
                .loanTypeId(request.getLoanTypeId())
                .statusId(request.getStatusId())
                .requestDate(request.getRequestDate())
                .lastUpdateDate(request.getLastUpdateDate())
                .userId(request.getUserId().userId())
                .build();
    }

    public Request toDomain(RequestEntity entity) {
        return Request.builder()
                .id(entity.getId())
                .name(new Name(entity.getName(),""))
                .documentNumber(new Identification(entity.getDocument()))
                .email(new Email(entity.getEmail()))
                .requestedAmount(new Amount(entity.getRequestedAmount()))
                .loanTerm(new LoanTerm(entity.getLoanTerm()))
                .loanTypeId(entity.getLoanTypeId())
                .statusId(entity.getStatusId())
                .requestDate(entity.getRequestDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .userId(new UserId(entity.getUserId()))
                .build();
    }
}
