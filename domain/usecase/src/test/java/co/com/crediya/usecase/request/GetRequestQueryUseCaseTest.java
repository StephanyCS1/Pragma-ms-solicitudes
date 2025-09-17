package co.com.crediya.usecase.request;

import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.Status;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.LoanTypeRepository;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.StatusRepository;
import co.com.crediya.model.solicitud.valueobjects.RequestDetailResponse;
import co.com.crediya.usecase.request.getallrequests.GetRequestQueryUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetRequestQueryUseCase Tests")
class GetRequestQueryUseCaseTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private LoanTypeRepository loanTypeRepository;
    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private GetRequestQueryUseCase getRequestQueryUseCase;

    private Request mockRequest;
    private LoanType mockLoanType;
    private Status mockStatus;

    @BeforeEach
    void setUp() {
        mockRequest = Request.builder()
                .id(UUID.randomUUID())
                .loanTypeId(UUID.randomUUID())
                .statusId(UUID.randomUUID())
                .requestDate(LocalDateTime.now())
                .build();
        mockLoanType = LoanType.builder().id(mockRequest.getLoanTypeId()).build();
        mockStatus = Status.builder().id(mockRequest.getStatusId()).build();
    }

    @Test
    @DisplayName("shouldThrowException_whenRequestNotFoundById")
    void getById_ShouldThrowException_WhenRequestNotFound() {
        when(requestRepository.findRequestById(mockRequest.getId())).thenReturn(Mono.empty());

        StepVerifier.create(getRequestQueryUseCase.getById(mockRequest.getId()))
                .expectErrorMatches(throwable -> throwable instanceof DomainValidationException &&
                        throwable.getMessage().contains("Solicitud no encontrada"))
                .verify();
    }

    @Test
    @DisplayName("shouldReturnAllRequests")
    void getAll_ShouldReturnFluxOfRequests() {
        when(requestRepository.findAllRequests()).thenReturn(Flux.just(mockRequest, new Request()));

        StepVerifier.create(getRequestQueryUseCase.getAll())
                .expectNextCount(2)
                .verifyComplete();
    }
}