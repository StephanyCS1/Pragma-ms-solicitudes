package co.com.crediya.usecase.request;

import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.usecase.request.createrequest.CreateRequestUseCase;
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


}
