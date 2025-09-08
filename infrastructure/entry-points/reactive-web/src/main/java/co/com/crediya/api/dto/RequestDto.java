package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record RequestDto(

        @NotBlank(message = "El teléfono es requerido")
        String phoneNumber,

        @NotNull(message = "El monto solicitado es requerido")
        @Positive(message = "El monto debe ser mayor a 0")
        BigDecimal requestedAmount,

        @NotNull(message = "El plazo del crédito es requerido")
        @Min(value = 6, message = "El plazo debe ser mínimo 6 meses")
        @Max(value = 360, message = "El plazo no puede exceder 360 meses")
        Integer loanTermMonths,

        @NotNull(message = "El tipo de crédito es requerido")
        UUID loanTypeId,

        @NotNull(message = "Los ingresos mensuales son requeridos")
        @Positive(message = "Los ingresos deben ser mayores a 0")
        BigDecimal monthlyIncome

) {}