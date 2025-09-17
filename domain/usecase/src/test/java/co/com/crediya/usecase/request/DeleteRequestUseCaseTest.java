package co.com.crediya.usecase.request;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.exceptions.RequestNotFoundException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.Email;
import co.com.crediya.usecase.request.deleterequest.DeleteRequestUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteRequestUseCase Tests")
class DeleteRequestUseCaseTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private DeleteRequestUseCase deleteRequestUseCase;

    private UUID requestId;
    private Email ownerEmail;
    private Request mockRequest;

    @BeforeEach
    void setUp() {
        requestId = UUID.randomUUID();
        ownerEmail = new Email("owner@example.com");
        mockRequest = Request.builder().id(requestId).email(ownerEmail).build();
    }

    @Test
    @DisplayName("shouldDeleteRequestSuccessfully_whenOwnerMatches")
    void excecute_ShouldSucceed_WhenOwnerMatches() {
        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));
        when(requestRepository.deleteRequest(requestId, ownerEmail.value())).thenReturn(Mono.empty());

        StepVerifier.create(deleteRequestUseCase.excecute(requestId.toString(), ownerEmail.value()))
                .verifyComplete();
    }

    @Test
    @DisplayName("shouldThrowException_whenRequestNotFound")
    void excecute_ShouldThrowException_WhenRequestNotFound() {
        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.empty());

        StepVerifier.create(deleteRequestUseCase.excecute(requestId.toString(), ownerEmail.value()))
                .expectErrorMatches(throwable -> throwable instanceof RequestNotFoundException &&
                        throwable.getMessage().contains("Solicitud no encontrada"))
                .verify();

        verify(requestRepository, never()).deleteRequest(any(), any());
    }

    @Test
    @DisplayName("shouldThrowException_whenUserIsNotOwner")
    void excecute_ShouldThrowException_WhenUserIsNotOwner() {
        Email anotherUserEmail = new Email("another@example.com");
        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));

        StepVerifier.create(deleteRequestUseCase.excecute(requestId.toString(), anotherUserEmail.value()))
                .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                        throwable.getMessage().contains("Solo el propietario puede eliminar la solicitud"))
                .verify();

        verify(requestRepository, never()).deleteRequest(any(), any());
    }
}