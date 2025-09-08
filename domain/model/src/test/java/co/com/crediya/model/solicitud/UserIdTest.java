package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.UserId;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void testUserIdCreation() {
        UUID uuid = UUID.randomUUID();
        UserId userId = new UserId(uuid);

        assertEquals(uuid, userId.userId());
    }

    @Test
    void testUserIdEquality() {
        UUID uuid = UUID.randomUUID();
        UserId userId1 = new UserId(uuid);
        UserId userId2 = new UserId(uuid);
        UserId userId3 = new UserId(UUID.randomUUID());

        assertEquals(userId1, userId2);
        assertEquals(userId1.hashCode(), userId2.hashCode());
        assertNotEquals(userId1, userId3);
    }

    @Test
    void testUserIdToString() {
        UUID uuid = UUID.randomUUID();
        UserId userId = new UserId(uuid);

        String expected = "UserId[userId=" + uuid + "]";
        assertEquals(expected, userId.toString());
    }

    @Test
    void testUserIdWithNullValue() {
        UserId userId = new UserId(null);
        assertNull(userId.userId());
    }
}