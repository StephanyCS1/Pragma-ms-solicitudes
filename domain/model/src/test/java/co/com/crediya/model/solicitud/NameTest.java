package co.com.crediya.model.solicitud;

import co.com.crediya.model.solicitud.valueobjects.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NameTest {

    @Test
    void testNameCreation() {
        Name name = new Name("juan", "pérez");

        assertEquals("juan", name.firstName());
        assertEquals("pérez", name.lastName());
    }

    @Test
    void testFullNameFormatting() {
        Name name = new Name("MARIA", "rodriguez");

        assertEquals("Maria Rodriguez", name.fullName());
    }

    @Test
    void testFullNameWithExtraSpaces() {
        Name name = new Name("  carlos  ", "  lópez  ");

        assertEquals("Carlos López", name.fullName());
    }

    @Test
    void testFullNameWithMixedCase() {
        Name name = new Name("aNa", "GaRcIa");

        assertEquals("Ana Garcia", name.fullName());
    }

    @Test
    void testFullNameWithNullValues() {
        Name name = new Name(null, "smith");

        assertEquals(" Smith", name.fullName());
    }

    @Test
    void testFullNameWithBlankValues() {
        Name name = new Name("", "johnson");

        assertEquals(" Johnson", name.fullName());
    }

    @Test
    void testFullNameBothNull() {
        Name name = new Name(null, null);

        assertEquals(" ", name.fullName());
    }
}