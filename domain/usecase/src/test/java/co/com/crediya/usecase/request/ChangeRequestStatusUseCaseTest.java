package co.com.crediya.usecase.request;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.usecase.request.changerequeststatus.ChangeRequestStatusUseCase;
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
@DisplayName("ChangeRequestStatusUseCase Tests")
class ChangeRequestStatusUseCaseTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ChangeRequestStatusUseCase changeRequestStatusUseCase;

    private UUID requestId;
    private Request mockRequest;

    @BeforeEach
    void setUp() {
        requestId = UUID.randomUUID();
        mockRequest = Request.builder().id(requestId).statusId(StatusName.PENDING_TO_CHECK.getId()).build();
    }

    @Test
    @DisplayName("shouldThrowException_whenInvalidStatusName")
    void update_ShouldThrowException_WhenInvalidStatusName() {
        StepVerifier.create(changeRequestStatusUseCase.update(requestId, "INVALID_STATUS"))
                .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                        throwable.getMessage().contains("Estado no válido: INVALID_STATUS"))
                .verify();

        verify(requestRepository, never()).findRequestById(any());
        verify(requestRepository, never()).updateStatus(any(), any());
    }

    @Test
    @DisplayName("shouldThrowException_whenStatusIsTheSame")
    void update_ShouldThrowException_WhenStatusIsTheSame() {
        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));

        StepVerifier.create(changeRequestStatusUseCase.update(requestId, "PENDING_TO_CHECK"))
                .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                        throwable.getMessage().contains("El estado debe ser diferente al actual"))
                .verify();

        verify(requestRepository, never()).updateStatus(any(), any());
    }
}