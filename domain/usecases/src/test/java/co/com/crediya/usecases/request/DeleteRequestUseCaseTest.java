package co.com.crediya.usecases.request;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.Email;
import co.com.crediya.usecases.request.deleterequest.DeleteRequestUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.UUID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRequestUseCaseTest {
    @Mock
    RequestRepository requestRepository;
    @InjectMocks
    DeleteRequestUseCase useCase;

    @Test
    void shouldDeleteWhenOwnerMatches() {
        UUID id = UUID.randomUUID();
        when(requestRepository.findRequestById(id)).thenReturn(Mono.just(Request.builder().id(id).email(new Email("owner@example.com")).build()));
        when(requestRepository.deleteRequest(id, "owner@example.com")).thenReturn(Mono.empty());
        StepVerifier.create(useCase.excecute(id.toString(), "owner@example.com")).verifyComplete();
    }

    @Test
    void shouldNotDeleteWhenOwnerDiffers() {
        UUID id = UUID.randomUUID();
        when(requestRepository.findRequestById(id)).thenReturn(Mono.just(Request.builder().id(id).email(new Email("owner@example.com")).build()));
        StepVerifier.create(useCase.excecute(id.toString(), "other@example.com")).verifyComplete();
        verify(requestRepository, never()).deleteRequest(any(), anyString());
    }
}
