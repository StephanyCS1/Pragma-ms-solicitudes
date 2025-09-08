package co.com.crediya.model.solicitud.valueobjects;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RequestSummary(
        UUID id,
        String applicantName,
        String email,
        BigDecimal amount,
        Integer termMonths,
        String loanType,
        BigDecimal interestRate,
        String status,
        BigDecimal baseSalary,
        BigDecimal monthlyPayment,
        Instant requestDate
) {}