package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entity.LoanTypeEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanTypeReactiveRepository extends ReactiveCrudRepository<LoanTypeEntity, UUID>,
        ReactiveQueryByExampleExecutor<LoanTypeEntity> {
    Mono<LoanTypeEntity> findIdByName(String name);
}
