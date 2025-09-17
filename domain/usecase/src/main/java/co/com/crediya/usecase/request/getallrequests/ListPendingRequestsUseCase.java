package co.com.crediya.usecase.request.getallrequests;

import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.enums.StatusName;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.gateways.LoanTypeRepository;
import co.com.crediya.model.solicitud.gateways.RequestRepository;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.model.solicitud.valueobjects.PagedResponse;
import co.com.crediya.model.solicitud.valueobjects.RequestSummary;
import co.com.crediya.model.solicitud.valueobjects.SortSpec;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ListPendingRequestsUseCase {

    private static final UUID ST_PENDING   = StatusName.PENDING_TO_CHECK.getId();
    private static final UUID ST_REJECTED  = StatusName.REJECTED.getId();
    private static final UUID ST_MANUAL_REVIEW = StatusName.MANUAL_REVIEW.getId();

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final MathContext MATH_CONTEXT = new MathContext(16, ROUNDING_MODE);
    private static final ZoneId BOGOTA_TZ = ZoneId.of("America/Bogota");

    private final RequestRepository requestRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final UserValidationService userValidationService;

    public Mono<PagedResponse<RequestSummary>> execute(
            int page, int size,
            String filterType, String filterValue,
            String  sortField,
            String direction,
            String token) {

        if (page < 0 || size <= 0) {
            return Mono.error(new DomainValidationException("Parámetros de paginación inválidos"));
        }
        var sort = new SortSpec(
                sortField,
                "DESC".equalsIgnoreCase(direction) ? SortSpec.Direction.DESC : SortSpec.Direction.ASC
        );
        String effectiveFilterType = filterType != null ? filterType : "";
        String effectiveFilterValue = filterValue != null ? filterValue : "";

        System.out.println(token);
        long limit = size;
        long offset = (long) page * size;

        var statuses = List.of(ST_PENDING, ST_REJECTED, ST_MANUAL_REVIEW);

        Mono<List<RequestSummary>> content = requestRepository
                .findPageByStatusesWithFilter(statuses, limit, offset, sort, effectiveFilterType, effectiveFilterValue)
                .flatMap(req -> enrichRequestWithDetails(req, token))
                .collectList();

        Mono<Long> total = requestRepository.countByStatusesWithFilter(statuses, effectiveFilterType, effectiveFilterValue);

        return Mono.zip(content, total)
                .map(t -> PagedResponse.of(t.getT1(), page, size, t.getT2()));
    }

    private Mono<RequestSummary> enrichRequestWithDetails(Request request, String token) {
        var loanData = loanTypeRepository.findLoanTypeById(request.getLoanTypeId())
                .switchIfEmpty(Mono.error(new DomainValidationException("Tipo de crédito no encontrado")))
                .map(lt -> Tuples.of(
                        lt.getName(),
                        Optional.ofNullable(lt.getInterestRate()).orElse("0")
                ));

        var baseSalary = userValidationService.findByEmail(request.getEmail().value(), token)
                .switchIfEmpty(Mono.error(new DomainValidationException("Usuario no encontrado")))
                .map(co.com.crediya.model.solicitud.valueobjects.pojo.UserResponse::baseSalary)
                .map(d -> d == null ? BigDecimal.ZERO : BigDecimal.valueOf(d))
                .map(bd -> bd.setScale(2, ROUNDING_MODE));

        return Mono.zip(loanData, baseSalary)
                .map(tuple -> {
                    var loanInfo = tuple.getT1();
                    BigDecimal salary = tuple.getT2();

                    String loanName = loanInfo.getT1();
                    BigDecimal interestRate = new BigDecimal(loanInfo.getT2());

                    BigDecimal monthlyPay = calculateMonthlyPayment(
                            request.getRequestedAmount().amount(),
                            request.getLoanTerm().months(),
                            interestRate
                    );

                    String statusName = StatusName.getNameById(request.getStatusId())
                            .orElse("UNKNOWN");

                    return new RequestSummary(
                            request.getId(),
                            request.getName().fullName(),
                            request.getEmail().value(),
                            request.getRequestedAmount().amount(),
                            request.getLoanTerm().months(),
                            loanName,
                            interestRate,
                            statusName,
                            salary,
                            monthlyPay,
                            request.getRequestDate().atZone(BOGOTA_TZ).toInstant()
                    );
                });
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, int months, BigDecimal monthlyRate) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || months <= 0) {
            return BigDecimal.ZERO;
        }
        if (monthlyRate == null || monthlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            return amount.divide(BigDecimal.valueOf(months), 2, ROUNDING_MODE);
        }
        BigDecimal onePlusI = BigDecimal.ONE.add(monthlyRate, MATH_CONTEXT);
        BigDecimal pow = onePlusI.pow(months, MATH_CONTEXT);
        BigDecimal numerator = amount.multiply(monthlyRate, MATH_CONTEXT);
        BigDecimal denominator = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(pow, MATH_CONTEXT), MATH_CONTEXT);
        if (denominator.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return numerator.divide(denominator, 2, ROUNDING_MODE);
    }
}