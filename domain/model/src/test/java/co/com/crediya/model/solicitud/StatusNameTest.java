package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.enums.StatusName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StatusNameTest {

    @Test
    void testStatusNameValues() {
        assertEquals(6, StatusName.values().length);

        assertNotNull(StatusName.INITITED);
        assertNotNull(StatusName.PROCESSING);
        assertNotNull(StatusName.PENDING_TO_CHECK);
        assertNotNull(StatusName.REJECTED);
        assertNotNull(StatusName.CANCEL_FOR_USER);
        assertNotNull(StatusName.APPROVED);
    }

    @Test
    void testStatusNameIds() {
        assertEquals(UUID.fromString("90c6d49c-cae5-464f-8594-17ca3510e79b"),
                StatusName.INITITED.getId());
        assertEquals(UUID.fromString("caae6c2c-14a6-4607-ba43-23d230ee901a"),
                StatusName.PROCESSING.getId());
        assertEquals(UUID.fromString("4f646f64-e460-43d1-9137-0201d4eb3743"),
                StatusName.PENDING_TO_CHECK.getId());
        assertEquals(UUID.fromString("dd8e57c4-598d-4c34-abe0-618cfe8c48e6"),
                StatusName.REJECTED.getId());
        assertEquals(UUID.fromString("28687783-cf20-40ef-8b63-37a855f2b930"),
                StatusName.CANCEL_FOR_USER.getId());
        assertEquals(UUID.fromString("25f823ed-fd38-46c0-824d-19382ff914fd"),
                StatusName.APPROVED.getId());
    }

    @Test
    void testFromIdMethod() {
        UUID initiatedId = UUID.fromString("90c6d49c-cae5-464f-8594-17ca3510e79b");
        Optional<StatusName> result = StatusName.fromId(initiatedId);

        assertTrue(result.isPresent());
        assertEquals(StatusName.INITITED, result.get());
    }

    @Test
    void testFromIdWithInvalidId() {
        UUID invalidId = UUID.randomUUID();
        Optional<StatusName> result = StatusName.fromId(invalidId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFromNameMethod() {
        Optional<StatusName> result = StatusName.fromName("APPROVED");

        assertTrue(result.isPresent());
        assertEquals(StatusName.APPROVED, result.get());
    }

    @Test
    void testFromNameWithInvalidName() {
        Optional<StatusName> result = StatusName.fromName("INVALID_STATUS");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFromNameCaseSensitive() {
        Optional<StatusName> result = StatusName.fromName("approved"); 

        assertTrue(result.isEmpty());
    }

    @Test
    void testToStringMethod() {
        String result = StatusName.PROCESSING.toString();
        assertEquals("caae6c2c-14a6-4607-ba43-23d230ee901a", result);
    }

    @Test
    void testAllStatusHaveUniqueIds() {
        Set<UUID> ids = Arrays.stream(StatusName.values())
                .map(StatusName::getId)
                .collect(Collectors.toSet());

        assertEquals(StatusName.values().length, ids.size());
    }
}