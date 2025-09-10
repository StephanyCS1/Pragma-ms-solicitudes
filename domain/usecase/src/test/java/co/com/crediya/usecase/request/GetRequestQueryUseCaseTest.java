package co.com.crediya.usecase.request;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.exceptions.RequestNotFoundException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.usecase.request.getallrequests.GetRequestQueryUseCase;
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
class GetRequestQueryUseCaseTest {
    @Mock
    RequestRepository requestRepository;
    @InjectMocks
    GetRequestQueryUseCase useCase;

    @Test
    void shouldGetById() {
        UUID id = UUID.randomUUID();
        when(requestRepository.findRequestById(id)).thenReturn(Mono.just(Request.builder().id(id).build()));
        StepVerifier.create(useCase.getById(id)).verifyComplete();
    }

    @Test
    void shouldErrorWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(requestRepository.findRequestById(id)).thenReturn(Mono.empty());
        StepVerifier.create(useCase.getById(id))
                .expectError(RequestNotFoundException.class)
                .verify();
    }
}
