package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.RequestDetailResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestDetailResponseTest {

    @Test
    void testRequestDetailResponseCreation() {
        UUID id = UUID.randomUUID();
        LoanType loanType = LoanType.builder()
                .id(UUID.randomUUID())
                .name("Personal")
                .build();
        Status status = Status.builder()
                .id(UUID.randomUUID())
                .name("PENDING")
                .build();
        LocalDateTime requestDate = LocalDateTime.now();

        RequestDetailResponse response = RequestDetailResponse.builder()
                .id(id)
                .documentNumber("12345678")
                .email("test@email.com")
                .requestedAmount(new BigDecimal("1000000"))
                .loanTerm(12)
                .requestDate(requestDate)
                .loanType(loanType)
                .status(status)
                .build();

        assertEquals(id, response.id());
        assertEquals("12345678", response.documentNumber());
        assertEquals("test@email.com", response.email());
        assertEquals(new BigDecimal("1000000"), response.requestedAmount());
        assertEquals(12, response.loanTerm());
        assertEquals(requestDate, response.requestDate());
        assertEquals(loanType, response.loanType());
        assertEquals(status, response.status());
    }

    @Test
    void testRequestDetailResponseEquality() {
        UUID id = UUID.randomUUID();
        LoanType loanType = LoanType.builder().name("Personal").build();
        Status status = Status.builder().name("APPROVED").build();

        RequestDetailResponse response1 = RequestDetailResponse.builder()
                .id(id)
                .documentNumber("11111111")
                .email("same@email.com")
                .requestedAmount(new BigDecimal("500000"))
                .loanTerm(24)
                .loanType(loanType)
                .status(status)
                .build();

        RequestDetailResponse response2 = RequestDetailResponse.builder()
                .id(id)
                .documentNumber("11111111")
                .email("same@email.com")
                .requestedAmount(new BigDecimal("500000"))
                .loanTerm(24)
                .loanType(loanType)
                .status(status)
                .build();

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }
}
