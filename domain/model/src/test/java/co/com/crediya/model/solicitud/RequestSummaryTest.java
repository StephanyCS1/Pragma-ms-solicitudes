package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.RequestSummary;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestSummaryTest {

    @Test
    void testRequestSummaryCreation() {
        UUID id = UUID.randomUUID();
        Instant requestDate = Instant.now();

        RequestSummary summary = new RequestSummary(
                id,
                "Juan Pérez",
                "juan@email.com",
                new BigDecimal("2000000"),
                36,
                "Personal",
                new BigDecimal("15.5"),
                "APPROVED",
                new BigDecimal("3000000"),
                new BigDecimal("85000"),
                requestDate
        );

        assertEquals(id, summary.id());
        assertEquals("Juan Pérez", summary.applicantName());
        assertEquals("juan@email.com", summary.email());
        assertEquals(new BigDecimal("2000000"), summary.amount());
        assertEquals(36, summary.termMonths());
        assertEquals("Personal", summary.loanType());
        assertEquals(new BigDecimal("15.5"), summary.interestRate());
        assertEquals("APPROVED", summary.status());
        assertEquals(new BigDecimal("3000000"), summary.baseSalary());
        assertEquals(new BigDecimal("85000"), summary.monthlyPayment());
        assertEquals(requestDate, summary.requestDate());
    }

    @Test
    void testRequestSummaryEquality() {
        UUID id = UUID.randomUUID();
        Instant date = Instant.parse("2024-01-15T10:30:00Z");

        RequestSummary summary1 = new RequestSummary(
                id, "Ana García", "ana@test.com", new BigDecimal("1500000"),
                24, "Hipotecario", new BigDecimal("8.5"), "PENDING",
                new BigDecimal("4000000"), new BigDecimal("120000"), date
        );

        RequestSummary summary2 = new RequestSummary(
                id, "Ana García", "ana@test.com", new BigDecimal("1500000"),
                24, "Hipotecario", new BigDecimal("8.5"), "PENDING",
                new BigDecimal("4000000"), new BigDecimal("120000"), date
        );

        assertEquals(summary1, summary2);
        assertEquals(summary1.hashCode(), summary2.hashCode());
    }
}