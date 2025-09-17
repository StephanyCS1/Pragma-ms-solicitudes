package co.com.crediya.usecase.request;

import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.CreateRequestCommand;
import co.com.crediya.usecase.request.createrequest.CreateRequestUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class CreateRequestUseCaseTest {

    RequestRepository requestRepository;
    GetLoanTypeQueryUseCase loanTypeUC;
    CreateRequestUseCase useCase;

    @BeforeEach
    void setUp() {
        requestRepository = mock(RequestRepository.class);
        loanTypeUC = mock(GetLoanTypeQueryUseCase.class);
        useCase = new CreateRequestUseCase(requestRepository, loanTypeUC);
    }


    @Test
    void create_errors_whenLoanTypeMissing() {
        CreateRequestCommand cmd = mock(CreateRequestCommand.class);
        UUID loanTypeId = UUID.randomUUID();
        when(cmd.loanTypeId()).thenReturn(loanTypeId);
        when(loanTypeUC.getById(loanTypeId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.create(cmd))
                .expectError(DomainValidationException.class)
                .verify();
    }
}