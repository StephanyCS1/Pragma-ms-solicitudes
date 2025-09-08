package co.com.crediya.usecases.status;

import co.com.crediya.model.solicitud.Status;
import co.com.crediya.model.solicitud.gateways.StatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllStatusesQueryUseCaseTest {
    @Mock
    StatusRepository statusRepository;
    @InjectMocks
    GetAllStatusesQueryUseCase useCase;

    @Test
    void shouldListAll() {
        when(statusRepository.findAllStatuses()).thenReturn(Flux.just(Status.builder().name("PENDING").build()));
        StepVerifier.create(useCase.getAll())
                .expectNextMatches(s -> s.getName().equals("PENDING"))
                .verifyComplete();
    }
}
