package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DomainValidationExceptionTest {

    @Test
    void testSingleMessageConstructor() {
        String message = "Test validation error";
        DomainValidationException exception = new DomainValidationException(message);

        assertEquals(message, exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertEquals(message, exception.getErrors().get(0));
    }

    @Test
    void testNullMessageConstructor() {
        DomainValidationException exception = new DomainValidationException((String) null);

        assertEquals(1, exception.getErrors().size());
        assertNull(exception.getErrors().get(0));
    }

    @Test
    void testMultipleErrorsConstructor() {
        List<String> errors = List.of("Error 1", "Error 2", "Error 3");
        DomainValidationException exception = new DomainValidationException(errors);

        assertEquals("Error 1; Error 2; Error 3", exception.getMessage());
        assertEquals(3, exception.getErrors().size());
        assertEquals(errors, exception.getErrors());
    }

    @Test
    void testEmptyErrorsList() {
        List<String> errors = Collections.emptyList();
        DomainValidationException exception = new DomainValidationException(errors);

        assertEquals("", exception.getMessage());
        assertTrue(exception.getErrors().isEmpty());
    }
}