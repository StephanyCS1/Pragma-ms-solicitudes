package co.com.crediya.config;

import co.com.crediya.model.solicitud.gateways.LoanTypeRepository;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.StatusRepository;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.usecase.loanType.GetLoanTypeQueryUseCase;
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
            GetLoanTypeQueryUseCase getLoanTypeQueryUseCase,
            UserValidationService userValidationService
    ) {
        return new CreateRequestUseCase(requestRepository, getLoanTypeQueryUseCase, userValidationService);
    }

    @Bean
    public ListPendingRequestsUseCase listPendingRequestsUseCase(RequestRepository requestRepository){
        return new ListPendingRequestsUseCase(requestRepository);
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
}