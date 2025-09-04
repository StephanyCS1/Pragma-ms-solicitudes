package co.com.crediya.model.solicitud.valueobjects;

import co.com.crediya.model.solicitud.LoanType;
import co.com.crediya.model.solicitud.Status;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record RequestDetailResponse(
        UUID id,
        String documentNumber,
        String email,
        BigDecimal requestedAmount,
        Integer loanTerm,
        LocalDateTime requestDate,
        LoanType loanType,
        Status status
) {}