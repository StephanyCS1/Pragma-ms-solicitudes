package co.com.crediya.usecase.request.createrequest;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.model.solicitud.valueobjects.CreateRequestCommand;
import co.com.crediya.model.solicitud.valueobjects.Name;
import co.com.crediya.model.solicitud.valueobjects.UserId;
import co.com.crediya.model.solicitud.valueobjects.pojo.UserResponse;
import co.com.crediya.usecase.loanType.GetLoanTypeQueryUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateRequestUseCase {

    private final RequestRepository requestRepository;
    private final GetLoanTypeQueryUseCase getLoanTypeQueryUseCase;
    private final UserValidationService userValidationService;

    public Mono<Request> create(CreateRequestCommand command) {
        if (command.userId() == null) {
            return Mono.error(new DomainValidationException("El userId es requerido"));
        }

        return userValidationService.findByEmail(command.email())
                .switchIfEmpty(Mono.error(new DomainValidationException("Usuario no encontrado")))
                .flatMap(user -> validateUserOwnership(user, command.email()))
                .flatMap(u -> getLoanTypeQueryUseCase.getById(command.loanTypeId()))
                .switchIfEmpty(Mono.error(new DomainValidationException("El tipo de crédito es incorrecto")))
                .then(save(command))
                .flatMap(requestRepository::save);
    }

    private Mono<UserResponse> validateUserOwnership(UserResponse user, String requestEmail) {
        if (!user.email().equalsIgnoreCase(requestEmail)) {
            return Mono.error(new DomainValidationException("No es permitido solicitar créditos a nombre de terceros"));
        }
        return Mono.just(user);
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