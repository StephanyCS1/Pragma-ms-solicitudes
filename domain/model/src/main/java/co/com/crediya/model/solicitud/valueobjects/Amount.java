package co.com.crediya.model.solicitud.valueobjects;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;

import java.math.BigDecimal;

public record Amount(BigDecimal amount) {

    public Amount {
        validate(amount);

    }

    private void validate(BigDecimal amount) {
        if(amount == null){
            throw new DomainValidationException("El monto es obligatorio");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0 && amount.compareTo(new BigDecimal(15000000)) != 0){
            throw new DomainValidationException("El monto debe ser mayor a cero");
        }
    }

}
