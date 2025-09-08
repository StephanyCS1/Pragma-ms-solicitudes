package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.exceptions.RequestNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestNotFoundExceptionTest {

    @Test
    void testRequestNotFoundExceptionCreation() {
        String message = "Request with ID 123 not found";
        RequestNotFoundException exception = new RequestNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testRequestNotFoundExceptionWithNullMessage() {
        RequestNotFoundException exception = new RequestNotFoundException(null);

        assertNull(exception.getMessage());
    }
}