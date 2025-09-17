package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.RequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RequestReactiveRepository extends ReactiveCrudRepository<RequestEntity, UUID>, ReactiveQueryByExampleExecutor<RequestEntity> {

    Flux<RequestEntity> findByEmail(String email);
    Flux<RequestEntity> findByEmail(String email, Pageable pageable);
    Flux<RequestEntity> findAllBy(Pageable pageable);

    Flux<RequestEntity> findByStatusId(UUID statusId, Pageable pageable);
    Mono<Long> countByStatusId(UUID statusId);

    Flux<RequestEntity> findByStatusIdInAndRequestedAmountLessThanEqual(List<UUID> statuses, BigDecimal max, Pageable pb);

    Flux<RequestEntity> findByStatusIdInAndRequestedAmountGreaterThanEqual(List<UUID> statuses, BigDecimal min, Pageable pb);

    Flux<RequestEntity> findByStatusIdInAndNameContainingIgnoreCase(List<UUID> statuses, String trim, Pageable pb);

    Flux<RequestEntity> findByStatusIdInAndEmailContainingIgnoreCase(List<UUID> statuses, String trim, Pageable pb);

    Flux<RequestEntity> findByStatusIdIn(List<UUID> statuses, Pageable pb);

    Mono<Long> countByStatusIdIn(List<UUID> statuses);

    Mono<Long> countByStatusIdInAndEmailContainingIgnoreCase(List<UUID> statuses, String trim);

    Mono<Long> countByStatusIdInAndNameContainingIgnoreCase(List<UUID> statuses, String trim);

    Mono<Long> countByStatusIdInAndRequestedAmountGreaterThanEqual(List<UUID> statuses, BigDecimal min);

    Mono<Long> countByStatusIdInAndRequestedAmountLessThanEqual(List<UUID> statuses, BigDecimal max);
}
