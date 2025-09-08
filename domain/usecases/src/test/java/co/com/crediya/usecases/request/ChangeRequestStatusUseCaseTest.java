package co.com.crediya.usecases.request;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.usecases.request.changerequeststatus.ChangeRequestStatusUseCase;
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
class ChangeRequestStatusUseCaseTest {
    @Mock
    RequestRepository requestRepository;
    @InjectMocks
    ChangeRequestStatusUseCase useCase;

    @Test
    void shouldUpdateStatus() {
        UUID req = UUID.randomUUID();
        UUID st = UUID.randomUUID();
        when(requestRepository.updateStatus(req, st)).thenReturn(Mono.just(Request.builder().id(req).statusId(st).build()));
        StepVerifier.create(useCase.update(req,"REJECTED"))
                .expectNextMatches(r -> r.getId().equals(req) && r.getStatusId().equals(st))
                .verifyComplete();
    }
}
