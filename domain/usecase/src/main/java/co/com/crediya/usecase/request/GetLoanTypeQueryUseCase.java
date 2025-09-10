package co.com.crediya.usecase.request;


import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class GetLoanTypeQueryUseCase {

    private final LoanTypeRepository repository;

    public Mono<LoanType> getByName(String name) {
        return repository.findLoanTypeByName(name);
    }

    public Mono<LoanType> getById(UUID id) {
        return repository.findLoanTypeById(id);
    }


}
