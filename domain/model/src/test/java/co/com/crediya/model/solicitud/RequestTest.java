package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void testRequestCreation() {
        UUID id = UUID.randomUUID();
        UUID loanTypeId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        Name name = new Name("Juan", "Pérez");
        Identification identification = new Identification("12345678");
        Email email = new Email("juan@email.com");
        Amount amount = new Amount(new BigDecimal("5000000"));
        LoanTerm loanTerm = new LoanTerm(12);
        UserId userId = new UserId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(id)
                .name(name)
                .documentNumber(identification)
                .email(email)
                .requestedAmount(amount)
                .loanTerm(loanTerm)
                .loanTypeId(loanTypeId)
                .statusId(statusId)
                .requestDate(now)
                .lastUpdateDate(now)
                .userId(userId)
                .build();

        assertEquals(id, request.getId());
        assertEquals(name, request.getName());
        assertEquals(identification, request.getDocumentNumber());
        assertEquals(email, request.getEmail());
        assertEquals(amount, request.getRequestedAmount());
        assertEquals(loanTerm, request.getLoanTerm());
        assertEquals(loanTypeId, request.getLoanTypeId());
        assertEquals(statusId, request.getStatusId());
        assertEquals(now, request.getRequestDate());
        assertEquals(now, request.getLastUpdateDate());
        assertEquals(userId, request.getUserId());
    }

    @Test
    void testRequestCreateMethod() {
        Name name = new Name("Ana", "García");
        String documentNumber = "98765432";
        String email = "ana@email.com";
        String requestedAmount = "3000000";
        Integer loanTermMonths = 24;
        UUID loanTypeId = UUID.randomUUID();
        UUID initialStatusId = UUID.randomUUID();
        UserId userId = new UserId(UUID.randomUUID());

        Request request = Request.create(
                name,
                documentNumber,
                email,
                requestedAmount,
                loanTermMonths,
                loanTypeId,
                initialStatusId,
                userId
        );

        assertNull(request.getId());
        assertEquals(name, request.getName());
        assertEquals(documentNumber, request.getDocumentNumber().document());
        assertEquals(email, request.getEmail().value());
        assertEquals(new BigDecimal(requestedAmount), request.getRequestedAmount().amount());
        assertEquals(loanTermMonths, request.getLoanTerm().months());
        assertEquals(loanTypeId, request.getLoanTypeId());
        assertEquals(initialStatusId, request.getStatusId());
        assertEquals(userId, request.getUserId());
        assertNotNull(request.getRequestDate());
        assertNotNull(request.getLastUpdateDate());
    }

    @Test
    void testRequestSettersAndGetters() {
        Request request = new Request();
        UUID id = UUID.randomUUID();
        Name name = new Name("Carlos", "López");

        request.setId(id);
        request.setName(name);

        assertEquals(id, request.getId());
        assertEquals(name, request.getName());
    }

    @Test
    void testRequestToBuilder() {
        UUID originalId = UUID.randomUUID();
        Name originalName = new Name("Pedro", "Martínez");

        Request original = Request.builder()
                .id(originalId)
                .name(originalName)
                .build();

        UUID newId = UUID.randomUUID();
        Request modified = original.toBuilder()
                .id(newId)
                .build();

        assertEquals(newId, modified.getId());
        assertEquals(originalName, modified.getName());
    }

    @Test
    void testRequestCreateWithValidData() {
        Name name = new Name("María", "Rodríguez");
        String documentNumber = "11111111";
        String email = "maria@test.com";
        String requestedAmount = "2500000";
        Integer loanTermMonths = 36;
        UUID loanTypeId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();
        UserId userId = new UserId(UUID.randomUUID());

        Request request = Request.create(
                name, documentNumber, email, requestedAmount,
                loanTermMonths, loanTypeId, statusId, userId
        );

        assertNotNull(request);
        assertEquals("11111111", request.getDocumentNumber().document());
        assertEquals("maria@test.com", request.getEmail().value());
        assertEquals(0, new BigDecimal("2500000").compareTo(request.getRequestedAmount().amount()));
        assertEquals(36, request.getLoanTerm().months());
        assertTrue(request.getRequestDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(request.getLastUpdateDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}