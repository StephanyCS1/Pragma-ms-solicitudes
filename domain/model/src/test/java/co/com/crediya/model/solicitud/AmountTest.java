package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.Amount;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountTest {

    @Test
    void testAmountCreationWithValidValue() {
        BigDecimal validAmount = new BigDecimal("1000000");
        Amount amount = new Amount(validAmount);

        assertEquals(validAmount, amount.amount());
    }

    @Test
    void testAmountWithSpecialValue15Million() {
        BigDecimal specialAmount = new BigDecimal("15000000");
        Amount amount = new Amount(specialAmount);

        assertEquals(specialAmount, amount.amount());
    }

    @Test
    void testAmountThrowsExceptionWhenNull() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Amount(null)
        );

        assertEquals("El monto es obligatorio", exception.getMessage());
    }

    @Test
    void testAmountThrowsExceptionWhenZero() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Amount(BigDecimal.ZERO)
        );

        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
    }

    @Test
    void testAmountThrowsExceptionWhenNegative() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Amount(new BigDecimal("-1000"))
        );

        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
    }
}