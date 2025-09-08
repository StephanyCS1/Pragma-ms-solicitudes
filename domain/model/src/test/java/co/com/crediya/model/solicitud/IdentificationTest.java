package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.Identification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdentificationTest {

    @Test
    void testValidIdentificationCreation() {
        String validDocument = "12345678";
        Identification identification = new Identification(validDocument);

        assertEquals(validDocument, identification.document());
    }

    @Test
    void testValidLongIdentification() {
        String validDocument = "123456789012345";
        Identification identification = new Identification(validDocument);

        assertEquals(validDocument, identification.document());
    }

    @Test
    void testNullDocumentThrowsException() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Identification(null)
        );

        assertEquals("El documento de identidad es obligatorio", exception.getMessage());
    }

    @Test
    void testBlankDocumentThrowsException() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Identification("   ")
        );

        assertEquals("El documento de identidad es obligatorio", exception.getMessage());
    }

    @Test
    void testShortDocumentThrowsException() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Identification("1234")
        );

        assertEquals("El documento debe tener entre 6 y 15 caracteres", exception.getMessage());
    }

    @Test
    void testLongDocumentThrowsException() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Identification("1234567890123456")
        );

        assertEquals("El documento debe tener entre 6 y 15 caracteres", exception.getMessage());
    }

    @Test
    void testDocumentWithLettersThrowsException() {
        Exception exception = assertThrows(
                co.com.crediya.model.solicitud.exceptions.DomainValidationException.class,
                () -> new Identification("12345A78")
        );

        assertEquals("El documento solo debe contener números", exception.getMessage());
    }
}
