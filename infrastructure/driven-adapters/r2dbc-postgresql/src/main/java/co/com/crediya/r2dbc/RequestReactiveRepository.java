package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.RequestEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface RequestReactiveRepository extends ReactiveCrudRepository<RequestEntity, UUID>, ReactiveQueryByExampleExecutor<RequestEntity> {

    Flux<RequestEntity> findByEmail(String email);
    Flux<RequestEntity> findByEmail(String email, Pageable pageable);
    Flux<RequestEntity> findAllBy(Pageable pageable);
    @Query("SELECT * FROM requests WHERE status_id = :statusId ORDER BY :#{#sort.toString()} LIMIT :limit OFFSET :offset")
    Flux<RequestEntity> findPageByStatusId(UUID statusId, long limit, long offset, Sort sort);

    @Query("SELECT COUNT(*) FROM requests WHERE status_id = :statusId")
    Mono<Long> countByStatusId(UUID statusId);
}
