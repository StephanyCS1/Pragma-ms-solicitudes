package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    void testValidEmailWithNumbers() {
        String validEmail = "user123@domain123.co";
        Email email = new Email(validEmail);

        assertEquals(validEmail, email.value());
    }

    @Test
    void testInvalidEmailThrowsException() {
        String invalidEmail = "invalid-email";

        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Email(invalidEmail)
        );

        assertEquals("El email no tiene el formato correcto", exception.getMessage());
    }


}
