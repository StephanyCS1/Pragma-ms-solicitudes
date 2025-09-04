package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.RequestEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface RequestReactiveRepository extends ReactiveCrudRepository<RequestEntity, UUID>, ReactiveQueryByExampleExecutor<RequestEntity> {

    Flux<RequestEntity> findByEmail(String email);
    Flux<RequestEntity> findAllByEmailAndStatusIdIn(String email, List<UUID> uuids);
}
