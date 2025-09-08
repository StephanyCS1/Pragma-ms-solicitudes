package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.LoanType;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanTypeRepository {

    Mono<LoanType> findLoanTypeById(UUID id);
    Mono<LoanType> findLoanTypeByName(String name);
}