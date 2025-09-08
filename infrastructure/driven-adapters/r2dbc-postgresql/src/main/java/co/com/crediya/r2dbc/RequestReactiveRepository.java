package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.RequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RequestReactiveRepository extends ReactiveCrudRepository<RequestEntity, UUID>, ReactiveQueryByExampleExecutor<RequestEntity> {

    Flux<RequestEntity> findByEmail(String email);
    Flux<RequestEntity> findByEmail(String email, Pageable pageable);
    Flux<RequestEntity> findAllBy(Pageable pageable);

    Flux<RequestEntity> findByStatusId(UUID statusId, Pageable pageable);
    Mono<Long> countByStatusId(UUID statusId);
}
