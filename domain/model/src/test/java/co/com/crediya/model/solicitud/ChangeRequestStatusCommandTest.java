package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.ChangeRequestStatusCommand;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeRequestStatusCommandTest {

    @Test
    void testChangeRequestStatusCommandCreation() {
        UUID requestId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        ChangeRequestStatusCommand command = new ChangeRequestStatusCommand(requestId, statusId);

        assertEquals(requestId, command.requestId());
        assertEquals(statusId, command.newStatusId());
    }

    @Test
    void testChangeRequestStatusCommandEquality() {
        UUID requestId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        ChangeRequestStatusCommand command1 = new ChangeRequestStatusCommand(requestId, statusId);
        ChangeRequestStatusCommand command2 = new ChangeRequestStatusCommand(requestId, statusId);

        assertEquals(command1, command2);
        assertEquals(command1.hashCode(), command2.hashCode());
    }
}