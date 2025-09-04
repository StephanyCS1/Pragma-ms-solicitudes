package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.*;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Request {

    private UUID id;
    private Identification documentNumber;
    private Email email;
    private Amount requestedAmount;
    private LoanTerm loanTerm;
    private UUID loanTypeId;
    private UUID statusId;
    private LocalDateTime requestDate;
    private LocalDateTime lastUpdateDate;


    public static Request create(
            String documentNumber,
            String email,
            String requestedAmount,
            Integer loanTermMonths,
            UUID loanTypeId,
            UUID initialStatusId) {

        return new Request(
                null,
                new Identification(documentNumber),
                new Email(email),
                new Amount(new BigDecimal(requestedAmount)),
                new LoanTerm(loanTermMonths),
                loanTypeId,
                initialStatusId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
