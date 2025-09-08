package co.com.crediya.model.solicitud.valueobjects;

import java.util.UUID;

public record CreateRequestCommand(

        String documentNumber,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String requestedAmount,
        Integer loanTermMonths,
        UUID loanTypeId,
        String monthlyIncome,
        UUID userId

) {}