package co.com.crediya.model.solicitud.valueobjects;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;

public record LoanTerm(int months) {
    public LoanTerm {
        validate(months);
    }

    public static void validate(int months) {
        if (months <= 0) {
            throw new DomainValidationException("El plazo debe ser mayor a cero");
        }
        if (months > 360) {
            throw new DomainValidationException("El plazo no puede exceder 360 meses");
        }
    }

    public String asString() {
        return String.valueOf(months);
    }
}