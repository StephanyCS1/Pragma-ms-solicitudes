package co.com.crediya.model.solicitud;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanTypeTest {

    @Test
    void testLoanTypeCreation() {
        UUID id = UUID.randomUUID();
        LoanType loanType = LoanType.builder()
                .id(id)
                .name("Préstamo Personal")
                .minAmount("1000000")
                .maxAmount("50000000")
                .interestRate("15.5")
                .automaticValidation("true")
                .build();

        assertEquals(id, loanType.getId());
        assertEquals("Préstamo Personal", loanType.getName());
        assertEquals("1000000", loanType.getMinAmount());
        assertEquals("50000000", loanType.getMaxAmount());
        assertEquals("15.5", loanType.getInterestRate());
        assertEquals("true", loanType.getAutomaticValidation());
    }

    @Test
    void testLoanTypeSettersAndGetters() {
        LoanType loanType = new LoanType();
        UUID id = UUID.randomUUID();

        loanType.setId(id);
        loanType.setName("Préstamo Hipotecario");
        loanType.setMinAmount("10000000");
        loanType.setMaxAmount("500000000");
        loanType.setInterestRate("8.5");
        loanType.setAutomaticValidation("false");

        assertEquals(id, loanType.getId());
        assertEquals("Préstamo Hipotecario", loanType.getName());
        assertEquals("10000000", loanType.getMinAmount());
        assertEquals("500000000", loanType.getMaxAmount());
        assertEquals("8.5", loanType.getInterestRate());
        assertEquals("false", loanType.getAutomaticValidation());
    }

    @Test
    void testLoanTypeToBuilder() {
        LoanType original = LoanType.builder()
                .id(UUID.randomUUID())
                .name("Original")
                .minAmount("1000")
                .build();

        LoanType modified = original.toBuilder()
                .name("Modified")
                .maxAmount("5000")
                .build();

        assertEquals("Modified", modified.getName());
        assertEquals("5000", modified.getMaxAmount());
        assertEquals(original.getId(), modified.getId());
        assertEquals(original.getMinAmount(), modified.getMinAmount());
    }
}