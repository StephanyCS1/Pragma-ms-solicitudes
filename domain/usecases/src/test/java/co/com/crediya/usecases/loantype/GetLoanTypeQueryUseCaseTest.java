package co.com.crediya.usecases.loantype;

import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.gateways.LoanTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.UUID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetLoanTypeQueryUseCaseTest {
    @Mock
    LoanTypeRepository loanTypeRepository;
    @InjectMocks
    GetLoanTypeQueryUseCase useCase;

    @Test
    void shouldReturnLoanType() {
        UUID id = UUID.randomUUID();
        when(loanTypeRepository.findLoanTypeById(id)).thenReturn(Mono.just(LoanType.builder().id(id).name("Libre").build()));
        StepVerifier.create(useCase.getById(id))
                .expectNextMatches(lt -> lt.getId().equals(id) && lt.getName().equals("Libre"))
                .verifyComplete();
    }
}
