package co.com.crediya.usecase.request.createrequest;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.CreateRequestCommand;
import co.com.crediya.model.solicitud.valueobjects.Name;
import co.com.crediya.model.solicitud.valueobjects.UserId;
import co.com.crediya.usecase.request.GetLoanTypeQueryUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateRequestUseCase {

    private final RequestRepository requestRepository;
    private final GetLoanTypeQueryUseCase getLoanTypeQueryUseCase;

    public Mono<Request> create(CreateRequestCommand command) {
        if (command.userId() == null) {
            return Mono.error(new DomainValidationException("El userId es requerido"));
        }
        System.out.println("Use Case");
        return Mono.just(command)
                .flatMap(u -> getLoanTypeQueryUseCase.getById(command.loanTypeId()))
                .switchIfEmpty(Mono.error(new DomainValidationException("El tipo de crédito es incorrecto")))
                .then(save(command))
                .flatMap(requestRepository::save);
    }


    private Mono<Request> save(CreateRequestCommand command) {
        Request request = Request.create(
                new Name(command.firstName(), command.lastName()),
                command.documentNumber(),
                command.email(),
                command.requestedAmount(),
                command.loanTermMonths(),
                command.loanTypeId(),
                StatusName.PENDING_TO_CHECK.getId(),
                new UserId(command.userId())
        );
        return Mono.just(request);
    }
}