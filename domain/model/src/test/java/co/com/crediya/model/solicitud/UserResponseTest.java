package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.pojo.UserResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void testUserResponseCreation() {
        UserResponse user = new UserResponse(
                "user123",
                "Carlos",
                "Mendoza",
                "carlos@email.com",
                2500000.0,
                "87654321",
                "CLIENT"
        );

        assertEquals("user123", user.uid());
        assertEquals("Carlos", user.name());
        assertEquals("Mendoza", user.lastName());
        assertEquals("carlos@email.com", user.email());
        assertEquals(2500000.0, user.baseSalary());
        assertEquals("87654321", user.identification());
        assertEquals("CLIENT", user.rol());
    }

    @Test
    void testUserResponseWithNullValues() {
        UserResponse user = new UserResponse(
                null, null, null, null, null, null, null
        );

        assertNull(user.uid());
        assertNull(user.name());
        assertNull(user.lastName());
        assertNull(user.email());
        assertNull(user.baseSalary());
        assertNull(user.identification());
        assertNull(user.rol());
    }

    @Test
    void testUserResponseEquality() {
        UserResponse user1 = new UserResponse(
                "user456", "Ana", "López", "ana@test.com",
                3000000.0, "12345678", "ADMIN"
        );

        UserResponse user2 = new UserResponse(
                "user456", "Ana", "López", "ana@test.com",
                3000000.0, "12345678", "ADMIN"
        );

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testUserResponseToString() {
        UserResponse user = new UserResponse(
                "test", "John", "Doe", "john@test.com",
                1500000.0, "99999999", "USER"
        );

        String result = user.toString();
        assertTrue(result.contains("test"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }
}
