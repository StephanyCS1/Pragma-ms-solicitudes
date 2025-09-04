package co.com.crediya.r2dbc;

import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.Status;
import co.com.crediya.model.solicitud.gateways.LoanTypeRepository;
import co.com.crediya.model.solicitud.gateways.StatusRepository;
import co.com.crediya.r2dbc.entity.LoanTypeEntity;
import co.com.crediya.r2dbc.entity.StatusEntity;
import co.com.crediya.r2dbc.helper.RequestReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends RequestReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        UUID,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {

    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanType.class));
    }


    @Override
    public Mono<LoanType> findLoanTypeById(UUID id) {
        return repository.findById(id).map(this::toEntity);
    }

    @Override
    public Mono<LoanType> findLoanTypeByName(String name) {
        return repository.findIdByName(name).map(this::toEntity);
    }

    @Override
    public Flux<LoanType> findAllLoanTypes() {
        return null;
    }

    @Override
    public Mono<Boolean> isAutomaticValidation(UUID id) {
        return repository.findById(id).map(LoanTypeEntity::isAutoValidation);
    }
}
