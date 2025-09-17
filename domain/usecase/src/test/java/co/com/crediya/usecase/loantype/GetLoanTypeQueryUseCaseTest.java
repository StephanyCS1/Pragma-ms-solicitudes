package co.com.crediya.usecase.loantype;

import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.gateways.LoanTypeRepository;
import co.com.crediya.usecase.request.GetLoanTypeQueryUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class GetLoanTypeQueryUseCaseTest {

    LoanTypeRepository repository;
    GetLoanTypeQueryUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(LoanTypeRepository.class);
        useCase = new GetLoanTypeQueryUseCase(repository);
    }

    @Test
    void getByName_delegatesToRepository() {
        LoanType lt = mock(LoanType.class);
        when(repository.findLoanTypeByName("LIBRE_INVERSION")).thenReturn(Mono.just(lt));

        StepVerifier.create(useCase.getByName("LIBRE_INVERSION"))
                .expectNext(lt)
                .verifyComplete();

        verify(repository).findLoanTypeByName("LIBRE_INVERSION");
    }

    @Test
    void getById_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        LoanType lt = mock(LoanType.class);
        when(repository.findLoanTypeById(id)).thenReturn(Mono.just(lt));

        StepVerifier.create(useCase.getById(id))
                .expectNext(lt)
                .verifyComplete();

        verify(repository).findLoanTypeById(id);
    }
}