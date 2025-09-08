package co.com.crediya.usecases.request;

import co.com.crediya.model.solicitud.valueobjects.PagedResponse;
import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.valueobjects.SortSpec;
import co.com.crediya.usecases.request.getallrequests.ListPendingRequestsUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPendingRequestsUseCaseTest {
    @Mock
    RequestRepository requestRepository;
    @InjectMocks
    ListPendingRequestsUseCase useCase;

    @Test
    void shouldListPending() {
        var resp = PagedResponse.of(List.of(Request.builder().build()), 0, 20, 1);
        SortSpec sort = new SortSpec("requestDate", SortSpec.Direction.DESC);
        when(requestRepository.listPending(0, 20, sort)).thenReturn(Mono.just(resp));
        StepVerifier.create(useCase.execute(0, 20, sort))
                .expectNext(resp)
                .verifyComplete();
    }
}
