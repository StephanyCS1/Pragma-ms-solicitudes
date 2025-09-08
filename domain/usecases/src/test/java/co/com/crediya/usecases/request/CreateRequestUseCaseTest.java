package co.com.crediya.usecases.request;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.model.solicitud.valueobjects.CreateRequestCommand;
import co.com.crediya.model.solicitud.valueobjects.Email;
import co.com.crediya.model.solicitud.valueobjects.pojo.UserResponse;
import co.com.crediya.usecases.request.createrequest.CreateRequestUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.UUID;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRequestUseCaseTest {
    @Mock
    RequestRepository requestRepository;
    @Mock
    UserValidationService userValidationService;
    @InjectMocks
    CreateRequestUseCase useCase;

    @Test
    void shouldCreateWhenUserExists() {
        var cmd = new CreateRequestCommand("Stephany","Castro","123","user@example.com",1_000_000L,12,UUID.randomUUID(),null,null,null,null);
        when(userValidationService.findByEmail(String.valueOf(new Email("user@example.com")))).thenReturn(Mono.just(new UserResponse("user@example.com","Stephany",null,null,null,null,null)));
        when(requestRepository.save(Mockito.any(Request.class))).thenAnswer(inv -> Mono.just(((Request) inv.getArgument(0)).toBuilder().id(UUID.randomUUID()).build()));
        StepVerifier.create(useCase.create(cmd))
                .expectNextMatches(r -> r.getId()!=null && r.getEmail().value().equals("user@example.com"))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenUserNotFound() {
        var cmd = new CreateRequestCommand("N","L","123","no@found.com",1_000_000L,12,UUID.randomUUID());
        when(userValidationService.findByEmail(new Email("no@found.com"))).thenReturn(Mono.empty());
        StepVerifier.create(useCase.create(cmd))
                .expectError(DomainValidationException.class)
                .verify();
    }
}
