package co.com.crediya.model.solicitud;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StatusTest {

    @Test
    void testStatusCreation() {
        UUID id = UUID.randomUUID();
        Status status = Status.builder()
                .id(id)
                .name("PENDING")
                .description("Solicitud pendiente de revisión")
                .build();

        assertEquals(id, status.getId());
        assertEquals("PENDING", status.getName());
        assertEquals("Solicitud pendiente de revisión", status.getDescription());
    }

    @Test
    void testStatusSettersAndGetters() {
        Status status = new Status();
        UUID id = UUID.randomUUID();

        status.setId(id);
        status.setName("APPROVED");
        status.setDescription("Solicitud aprobada");

        assertEquals(id, status.getId());
        assertEquals("APPROVED", status.getName());
        assertEquals("Solicitud aprobada", status.getDescription());
    }

    @Test
    void testStatusAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        Status status = new Status(id, "REJECTED", "Solicitud rechazada");

        assertEquals(id, status.getId());
        assertEquals("REJECTED", status.getName());
        assertEquals("Solicitud rechazada", status.getDescription());
    }

    @Test
    void testStatusNoArgsConstructor() {
        Status status = new Status();

        assertNull(status.getId());
        assertNull(status.getName());
        assertNull(status.getDescription());
    }
}