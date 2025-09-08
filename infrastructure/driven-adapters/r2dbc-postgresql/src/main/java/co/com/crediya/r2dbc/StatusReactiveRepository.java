package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.StatusEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface StatusReactiveRepository extends ReactiveCrudRepository<StatusEntity, UUID>,
        ReactiveQueryByExampleExecutor<StatusEntity> {
    Mono<StatusEntity> findIdByName(String name);
}
