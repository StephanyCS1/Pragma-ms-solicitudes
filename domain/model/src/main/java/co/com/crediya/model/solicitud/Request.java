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
    private Name name;
    private Identification documentNumber;
    private Email email;
    private Amount requestedAmount;
    private LoanTerm loanTerm;
    private UUID loanTypeId;
    private UUID statusId;
    private LocalDateTime requestDate;
    private LocalDateTime lastUpdateDate;
    private UserId userId;

    public static Request create(
            Name name,
            String documentNumber,
            String email,
            String requestedAmount,
            Integer loanTermMonths,
            UUID loanTypeId,
            UUID initialStatusId,
            UserId userId) {

        return new Request(
                null,
                name,
                new Identification(documentNumber),
                new Email(email),
                new Amount(new BigDecimal(requestedAmount)),
                new LoanTerm(loanTermMonths),
                loanTypeId,
                initialStatusId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                userId
        );
    }
}
