package co.com.crediya.usecase.request.getallrequests;

import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.Status;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.LoanTypeRepository;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.StatusRepository;
import co.com.crediya.model.solicitud.valueobjects.RequestDetailResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class GetRequestQueryUseCase {

    private final RequestRepository requestRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;

    public Flux<Request> getAll() {
        return requestRepository.findAllRequests();
    }

    public Mono<RequestDetailResponse> getById(UUID id){
        return requestRepository.findRequestById(id)
                .switchIfEmpty(Mono.error(new DomainValidationException("Solicitud no encontrada")))
                .flatMap(request ->
                        Mono.zip(
                                Mono.just(request),
                                loanTypeRepository.findLoanTypeById(request.getLoanTypeId()),
                                statusRepository.findStatusById(request.getStatusId())
                        ).map(tuple -> buildDetailResponse(tuple.getT1(), tuple.getT2(), tuple.getT3()))
                );
    }

    private RequestDetailResponse buildDetailResponse(Request request, LoanType loanType, Status status) {
        return RequestDetailResponse.builder()
                .id(request.getId())
                .documentNumber(request.getDocumentNumber().document())
                .email(request.getEmail().value())
                .requestedAmount(request.getRequestedAmount().amount())
                .loanTerm(request.getLoanTerm().months())
                .requestDate(request.getRequestDate())
                .loanType(loanType)
                .status(status)
                .build();
    }
}
