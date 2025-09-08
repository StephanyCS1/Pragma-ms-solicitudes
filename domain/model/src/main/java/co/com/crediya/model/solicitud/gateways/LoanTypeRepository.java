package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.valueobjects.Amount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanTypeRepository {

    Mono<LoanType> findLoanTypeById(UUID id);
    Mono<LoanType> findLoanTypeByName(String name);
    Flux<LoanType> findAllLoanTypes();
    Mono<Boolean> isAutomaticValidation(UUID id);
}