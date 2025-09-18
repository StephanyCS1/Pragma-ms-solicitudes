package co.com.crediya.usecase.request;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.SendNotification;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.model.solicitud.valueobjects.Email;
import co.com.crediya.model.solicitud.valueobjects.SendNotificationData;
import co.com.crediya.model.solicitud.valueobjects.pojo.UserResponse;
import co.com.crediya.usecase.request.changerequeststatus.ChangeRequestStatusUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeRequestStatusUseCaseTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private SendNotification sendNotification;

    @Mock
    private UserValidationService userValidationService;

    @InjectMocks
    private ChangeRequestStatusUseCase useCase;

    private UUID requestId;
    private UUID initialStatusId;
    private UUID approvedStatusId;
    private UUID rejectedStatusId;
    private Request mockRequest;
    private String token;
    private String userEmail;

    @BeforeEach
    void setUp() {
        requestId = UUID.randomUUID();
        initialStatusId = StatusName.INITITED.getId();
        approvedStatusId = StatusName.APPROVED.getId();
        rejectedStatusId = StatusName.REJECTED.getId();
        userEmail = "test@example.com";
        token = "test-token";

        mockRequest = Request.builder()
                .id(requestId)
                .statusId(initialStatusId)
                .email(new Email(userEmail))
                .requestDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldUpdateStatusAndSendNotificationOnApproved() {
        String newStatusName = "APPROVED";
        UserResponse mockUserResponse = new UserResponse(
                UUID.randomUUID().toString(),
                "Test User",
                "Last Name",
                userEmail,
                1000.0,
                "123456789",
                "ROL_TEST"
        );
        Request updatedRequest = mockRequest.toBuilder().statusId(approvedStatusId).build();

        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));
        when(requestRepository.updateStatus(requestId, approvedStatusId)).thenReturn(Mono.just(updatedRequest));
        when(userValidationService.findByEmail(userEmail, token)).thenReturn(Mono.just(mockUserResponse));
        when(sendNotification.sendNotification(any(SendNotificationData.class), eq(token))).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.update(requestId, newStatusName, token))
                .expectNextMatches(req -> req.getId().equals(requestId) && req.getStatusId().equals(approvedStatusId))
                .verifyComplete();

        verify(requestRepository).findRequestById(requestId);
        verify(requestRepository).updateStatus(requestId, approvedStatusId);
        verify(userValidationService).findByEmail(userEmail, token);
        verify(sendNotification).sendNotification(any(SendNotificationData.class), eq(token));
    }

    @Test
    void shouldUpdateStatusAndSendNotificationOnRejected() {
        String newStatusName = "REJECTED";
        UserResponse mockUserResponse = new UserResponse(
                UUID.randomUUID().toString(),
                "Test User",
                "Last Name",
                userEmail,
                1000.0,
                "123456789",
                "ROL_TEST"
        );
        Request updatedRequest = mockRequest.toBuilder().statusId(rejectedStatusId).build();

        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));
        when(requestRepository.updateStatus(requestId, rejectedStatusId)).thenReturn(Mono.just(updatedRequest));
        when(userValidationService.findByEmail(userEmail, token)).thenReturn(Mono.just(mockUserResponse));
        when(sendNotification.sendNotification(any(SendNotificationData.class), eq(token))).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.update(requestId, newStatusName, token))
                .expectNextMatches(req -> req.getId().equals(requestId) && req.getStatusId().equals(rejectedStatusId))
                .verifyComplete();

        verify(requestRepository).findRequestById(requestId);
        verify(requestRepository).updateStatus(requestId, rejectedStatusId);
        verify(userValidationService).findByEmail(userEmail, token);
        verify(sendNotification).sendNotification(any(SendNotificationData.class), eq(token));
    }

    @Test
    void shouldUpdateStatusAndNotSendNotificationOnOtherStatus() {
        String newStatusName = "PENDING_TO_CHECK";
        UUID newStatusId = StatusName.PENDING_TO_CHECK.getId();
        Request updatedRequest = mockRequest.toBuilder().statusId(newStatusId).build();

        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));
        when(requestRepository.updateStatus(requestId, newStatusId)).thenReturn(Mono.just(updatedRequest));

        StepVerifier.create(useCase.update(requestId, newStatusName, token))
                .expectNextMatches(req -> req.getId().equals(requestId) && req.getStatusId().equals(newStatusId))
                .verifyComplete();

        verify(requestRepository).findRequestById(requestId);
        verify(requestRepository).updateStatus(requestId, newStatusId);
        verifyNoInteractions(userValidationService);
        verifyNoInteractions(sendNotification);
    }

    @Test
    void shouldReturnErrorForInvalidStatusName() {
        String newStatusName = "INVALID_STATUS";

        StepVerifier.create(useCase.update(requestId, newStatusName, token))
                .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                        throwable.getMessage().equals("Estado no válido: INVALID_STATUS"))
                .verify();

        verifyNoInteractions(requestRepository);
        verifyNoInteractions(userValidationService);
        verifyNoInteractions(sendNotification);
    }

    @Test
    void shouldReturnErrorWhenRequestNotFound() {
        String newStatusName = "APPROVED";

        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.update(requestId, newStatusName, token))
                .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                        throwable.getMessage().equals("Solicitud no encontrada"))
                .verify();

        verify(requestRepository).findRequestById(requestId);
        verifyNoMoreInteractions(requestRepository);
        verifyNoInteractions(userValidationService);
        verifyNoInteractions(sendNotification);
    }

    @Test
    void shouldReturnErrorWhenStatusIsTheSame() {
        String newStatusName = "INITITED"; // Same as the initial status

        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));

        StepVerifier.create(useCase.update(requestId, newStatusName, token))
                .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                        throwable.getMessage().equals("El estado debe ser diferente al actual"))
                .verify();

        verify(requestRepository).findRequestById(requestId);
        verifyNoMoreInteractions(requestRepository);
        verifyNoInteractions(userValidationService);
        verifyNoInteractions(sendNotification);
    }

    @Test
    void shouldHandleUserNotFoundForNotificationAndReturnRequest() {
        String newStatusName = "APPROVED";
        Request updatedRequest = mockRequest.toBuilder().statusId(approvedStatusId).build();

        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));
        when(requestRepository.updateStatus(requestId, approvedStatusId)).thenReturn(Mono.just(updatedRequest));
        when(userValidationService.findByEmail(userEmail, token)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.update(requestId, newStatusName, token))
                .expectNextMatches(req -> req.getId().equals(requestId) && req.getStatusId().equals(approvedStatusId))
                .verifyComplete();

        verify(requestRepository).findRequestById(requestId);
        verify(requestRepository).updateStatus(requestId, approvedStatusId);
        verify(userValidationService).findByEmail(userEmail, token);
        verifyNoInteractions(sendNotification);
    }

    @Test
    void shouldHandleNotificationServiceErrorAndReturnRequest() {
        String newStatusName = "APPROVED";
        UserResponse mockUserResponse = new UserResponse(
                UUID.randomUUID().toString(),
                "Test User",
                "Last Name",
                userEmail,
                1000.0,
                "123456789",
                "ROL_TEST"
        );
        Request updatedRequest = mockRequest.toBuilder().statusId(approvedStatusId).build();

        when(requestRepository.findRequestById(requestId)).thenReturn(Mono.just(mockRequest));
        when(requestRepository.updateStatus(requestId, approvedStatusId)).thenReturn(Mono.just(updatedRequest));
        when(userValidationService.findByEmail(userEmail, token)).thenReturn(Mono.just(mockUserResponse));
        when(sendNotification.sendNotification(any(SendNotificationData.class), eq(token)))
                .thenReturn(Mono.error(new RuntimeException("Notification service failed")));

        StepVerifier.create(useCase.update(requestId, newStatusName, token))
                .expectNextMatches(req -> req.getId().equals(requestId) && req.getStatusId().equals(approvedStatusId))
                .verifyComplete();

        verify(requestRepository).findRequestById(requestId);
        verify(requestRepository).updateStatus(requestId, approvedStatusId);
        verify(userValidationService).findByEmail(userEmail, token);
        verify(sendNotification).sendNotification(any(SendNotificationData.class), eq(token));
    }
}