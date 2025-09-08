package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.CreateRequestCommand;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateRequestCommandTest {

    @Test
    void testCreateRequestCommandCreation() {
        UUID loanTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CreateRequestCommand command = new CreateRequestCommand(
                "12345678",
                "Juan",
                "Pérez",
                "juan@email.com",
                "3001234567",
                "5000000",
                24,
                loanTypeId,
                "3000000",
                userId
        );

        assertEquals("12345678", command.documentNumber());
        assertEquals("Juan", command.firstName());
        assertEquals("Pérez", command.lastName());
        assertEquals("juan@email.com", command.email());
        assertEquals("3001234567", command.phoneNumber());
        assertEquals("5000000", command.requestedAmount());
        assertEquals(24, command.loanTermMonths());
        assertEquals(loanTypeId, command.loanTypeId());
        assertEquals("3000000", command.monthlyIncome());
        assertEquals(userId, command.userId());
    }

    @Test
    void testCreateRequestCommandEquality() {
        UUID loanTypeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CreateRequestCommand command1 = new CreateRequestCommand(
                "11111111", "Ana", "García", "ana@test.com", "3009876543",
                "2000000", 36, loanTypeId, "4000000", userId
        );

        CreateRequestCommand command2 = new CreateRequestCommand(
                "11111111", "Ana", "García", "ana@test.com", "3009876543",
                "2000000", 36, loanTypeId, "4000000", userId
        );

        assertEquals(command1, command2);
        assertEquals(command1.hashCode(), command2.hashCode());
    }
}