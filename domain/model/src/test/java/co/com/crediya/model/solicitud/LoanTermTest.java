package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.LoanTerm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoanTermTest {

    @Test
    void testValidLoanTermCreation() {
        int validMonths = 24;
        LoanTerm loanTerm = new LoanTerm(validMonths);

        assertEquals(validMonths, loanTerm.months());
    }

    @Test
    void testMaxValidLoanTerm() {
        int maxMonths = 360;
        LoanTerm loanTerm = new LoanTerm(maxMonths);

        assertEquals(maxMonths, loanTerm.months());
    }

    @Test
    void testMinValidLoanTerm() {
        int minMonths = 1;
        LoanTerm loanTerm = new LoanTerm(minMonths);

        assertEquals(minMonths, loanTerm.months());
    }

    @Test
    void testLoanTermAsString() {
        LoanTerm loanTerm = new LoanTerm(36);

        assertEquals("36", loanTerm.asString());
    }

    @Test
    void testZeroMonthsThrowsException() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new LoanTerm(0)
        );

        assertEquals("El plazo debe ser mayor a cero", exception.getMessage());
    }

    @Test
    void testNegativeMonthsThrowsException() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new LoanTerm(-12)
        );

        assertEquals("El plazo debe ser mayor a cero", exception.getMessage());
    }

    @Test
    void testExcessiveMonthsThrowsException() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new LoanTerm(361)
        );

        assertEquals("El plazo no puede exceder 360 meses", exception.getMessage());
    }
}