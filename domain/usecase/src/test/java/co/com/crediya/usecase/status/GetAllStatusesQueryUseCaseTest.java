package co.com.crediya.usecase.status;

import co.com.crediya.model.solicitud.Status;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllStatusesQueryUseCaseTest {
    StatusRepository repository;
    GetAllStatusesQueryUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(StatusRepository.class);
        useCase = new GetAllStatusesQueryUseCase(repository);
    }

    @Test
    void getAll_returnsFluxFromRepository() {
        when(repository.findAllStatuses()).thenReturn(Flux.just(mock(Status.class), mock(Status.class)));

        StepVerifier.create(useCase.getAll())
                .expectNextCount(2)
                .verifyComplete();

        verify(repository).findAllStatuses();
    }

    @Test
    void getById_found() {
        UUID id = UUID.randomUUID();
        when(repository.findStatusById(id)).thenReturn(Mono.just(mock(Status.class)));

        StepVerifier.create(useCase.getById(id))
                .expectNextCount(1)
                .verifyComplete();

        verify(repository).findStatusById(id);
    }

    @Test
    void getById_notFound_emitsDomainValidationException() {
        UUID id = UUID.randomUUID();
        when(repository.findStatusById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getById(id))
                .expectError(DomainValidationException.class)
                .verify();
    }

    @Test
    void getByName_found() {
        when(repository.findIdByName("PENDING")).thenReturn(Mono.just(mock(Status.class)));

        StepVerifier.create(useCase.getByName("PENDING"))
                .expectNextCount(1)
                .verifyComplete();

        verify(repository).findIdByName("PENDING");
    }

    @Test
    void getByName_notFound_emitsDomainValidationException() {
        when(repository.findIdByName("MISSING")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getByName("MISSING"))
                .expectError(DomainValidationException.class)
                .verify();
    }
}
