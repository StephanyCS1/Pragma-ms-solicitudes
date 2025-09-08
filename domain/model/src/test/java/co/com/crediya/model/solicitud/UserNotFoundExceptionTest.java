package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserNotFoundExceptionTest {

    @Test
    void testUserNotFoundExceptionCreation() {
        String message = "User with ID user123 not found";
        UserNotFoundException exception = new UserNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testUserNotFoundExceptionWithEmptyMessage() {
        UserNotFoundException exception = new UserNotFoundException("");

        assertEquals("", exception.getMessage());
    }
}