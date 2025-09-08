package co.com.crediya.r2dbc;

import co.com.crediya.model.solicitud.Status;
import co.com.crediya.model.solicitud.gateways.StatusRepository;
import co.com.crediya.r2dbc.entity.StatusEntity;
import co.com.crediya.r2dbc.helper.RequestReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
public class StatusReactiveRepositoryAdapter extends RequestReactiveAdapterOperations<
        Status,
        StatusEntity,
        UUID,
        StatusReactiveRepository
        > implements StatusRepository {

    public StatusReactiveRepositoryAdapter(StatusReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Status.class));
    }


    @Override
    public Mono<Status> findStatusById(UUID id) {
        return repository.findById(id).map(this::toEntity);
    }

    @Override
    public Mono<Status> findIdByName(String name) {
        return repository.findIdByName(name).map(this::toEntity);
    }

    @Override
    public Flux<Status> findAllStatuses() {
        return repository.findAll().map(this::toEntity);
    }
}
