package co.com.crediya.config;

import co.com.crediya.model.solicitud.gateways.*;
import co.com.crediya.usecase.request.GetLoanTypeQueryUseCase;
import co.com.crediya.usecase.request.changerequeststatus.ChangeRequestStatusUseCase;
import co.com.crediya.usecase.request.createrequest.CreateRequestUseCase;
import co.com.crediya.usecase.request.getallrequests.GetRequestQueryUseCase;
import co.com.crediya.usecase.request.getallrequests.ListPendingRequestsUseCase;
import co.com.crediya.usecase.status.GetAllStatusesQueryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {


    @Bean
    public CreateRequestUseCase createRequestUseCase(
            RequestRepository requestRepository,
            GetLoanTypeQueryUseCase getLoanTypeQueryUseCase
    ) {
        return new CreateRequestUseCase(requestRepository, getLoanTypeQueryUseCase);
    }

    @Bean
    public ListPendingRequestsUseCase listPendingRequestsUseCase(RequestRepository requestRepository,
                                                                 LoanTypeRepository loanTypeRepository,
                                                                 UserValidationService userValidationService){
        return new ListPendingRequestsUseCase(requestRepository, loanTypeRepository, userValidationService);
    }

    @Bean
    public GetLoanTypeQueryUseCase getLoanTypeQueryUseCase(LoanTypeRepository loanTypeRepository) {
        return new GetLoanTypeQueryUseCase(loanTypeRepository);
    }

    @Bean
    public GetRequestQueryUseCase getRequestQueryUseCase(
            RequestRepository requestRepository,
            LoanTypeRepository loanTypeRepository,
            StatusRepository statusRepository) {
        return new GetRequestQueryUseCase(requestRepository, loanTypeRepository, statusRepository);
    }

    @Bean
    public GetAllStatusesQueryUseCase getAllStatusesQueryUseCase(StatusRepository statusRepository) {
        return new GetAllStatusesQueryUseCase(statusRepository);
    }

    @Bean
    public ChangeRequestStatusUseCase changeRequestStatusUseCase(RequestRepository requestRepository,
                                                                 SendNotification sendNotification,
                                                                 UserValidationService userValidationService) {
        return new ChangeRequestStatusUseCase(requestRepository, sendNotification, userValidationService);
    }
}