package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void testValidationExceptionCreation() {
        String message = "Validation failed";
        ValidationException exception = new ValidationException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testValidationExceptionConstants() {
        assertEquals("INVALID_USER_DATA", ValidationException.INVALID_USER_DATA);
        assertEquals("INVALID_UUID_FORMAT", ValidationException.INVALID_UUID_FORMAT);
        assertEquals("INVALID_EMAIL_FORMAT", ValidationException.INVALID_EMAIL_FORMAT);
        assertEquals("INVALID_DATE_FORMAT", ValidationException.INVALID_DATE_FORMAT);
    }

    @Test
    void testValidationExceptionWithConstants() {
        ValidationException exception = new ValidationException(ValidationException.INVALID_EMAIL_FORMAT);

        assertEquals("INVALID_EMAIL_FORMAT", exception.getMessage());
    }

    @Test
    void testAllConstantsAreStrings() {
        assertNotNull(ValidationException.INVALID_USER_DATA);
        assertNotNull(ValidationException.INVALID_UUID_FORMAT);
        assertNotNull(ValidationException.INVALID_EMAIL_FORMAT);
        assertNotNull(ValidationException.INVALID_DATE_FORMAT);

        assertTrue(ValidationException.INVALID_USER_DATA instanceof String);
        assertTrue(ValidationException.INVALID_UUID_FORMAT instanceof String);
        assertTrue(ValidationException.INVALID_EMAIL_FORMAT instanceof String);
        assertTrue(ValidationException.INVALID_DATE_FORMAT instanceof String);
    }
}